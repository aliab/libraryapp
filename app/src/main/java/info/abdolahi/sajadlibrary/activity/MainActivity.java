package info.abdolahi.sajadlibrary.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.abdolahi.sajadlibrary.R;
import info.abdolahi.sajadlibrary.adapter.BooksListAdapter;
import info.abdolahi.sajadlibrary.db.BookModel;
import info.abdolahi.sajadlibrary.db.DataBaseHelper;
import info.abdolahi.sajadlibrary.db.SearchModel;
import info.abdolahi.sajadlibrary.db.TextCellDataSource;
import info.abdolahi.sajadlibrary.dialog.ShowBookDetailDialogFragment;

public class MainActivity extends AppCompatActivity implements BooksListAdapter.OnRecyclerViewItemClickListener, ShowBookDetailDialogFragment.faveMeDialogListener {

    @Bind(R.id.rv)
    RecyclerView rv;

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;

    @Bind(R.id.toolbar_title)
    TextView toolbar_title;

    @Bind(R.id.container)
    CoordinatorLayout container;

    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    @Bind(R.id.list_empty)
    ImageView listEmpty;


    TextCellDataSource dataSource;
    BooksListAdapter mAdapter;
    List<BookModel> mData;

    boolean isFavedIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("isfaved")) {
            isFavedIntent = true;
        }

        init_app();
        init_view();
    }


    private void checkAdapterIsEmpty() {
        if (mAdapter.getItemCount() == 0) {
            listEmpty.setVisibility(View.VISIBLE);
        } else {
            listEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * init view config
     */
    private void init_view() {

        // make toolbar available as actionbar
        setSupportActionBar(toolbar);

        // force toolbar to hide title
        getSupportActionBar().setTitle(null);

        if (isFavedIntent) {
            toolbar_title.setText(getString(R.string.action_favorites));
        }

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<SearchModel> searchModels = new ArrayList<>();
                SearchModel searchModel = new SearchModel();
                searchModel.setFieldName(DataBaseHelper.TITLE);
                searchModel.setFieldValue(newText);
                searchModels.add(searchModel);

                if (isFavedIntent) {
                    mData = dataSource.searchInFavoriteDB(searchModels);
                } else {
                    mData = dataSource.searchInDB(searchModels);
                }
                mAdapter.notifyDataSetChanged();
                displayList();
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        /**
         * request for getting data and setting into list
         */
        displayList();
    }


    /**
     * get data from database and set it to list
     */
    private void displayList() {

        mAdapter = new BooksListAdapter(mData);
        mAdapter.setOnItemClickListener(this);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });

        rv.setAdapter(mAdapter);
        checkAdapterIsEmpty();
    }

    /**
     * all things belong to first app init
     */
    private void init_app() {

        /**
         * init database
         */
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataSource = new TextCellDataSource(this);

        if (!isFavedIntent) {
            mData = dataSource.findAllTitle();
        } else {
            mData = dataSource.findAllfaved();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        if (isFavedIntent) {
            MenuItem itemShowFaved = menu.findItem(R.id.action_showfaved);
            MenuItem itemAboutUS = menu.findItem(R.id.action_aboutus);
            itemShowFaved.setVisible(false);
            itemAboutUS.setVisible(false);
            this.invalidateOptionsMenu();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aboutus) {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle(getString(R.string.app_name) + " " + getString(R.string.app_version));

            StringBuilder sb = new StringBuilder();
            sb.append("ساخته شده در دپارتمان صنایع دانشگاه سجاد");
            sb.append("\n");
            sb.append("توسط علی عبداللهی");
            sb.append("\n");
            sb.append("پاییز ۱۳۹۴");

            dialog.setMessage(sb.toString());
            dialog.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog.create().show();

            return true;
        } else if (id == R.id.action_showfaved) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("isfaved", true);
            startActivityForResult(i, 1001);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (!isFavedIntent) {
                mData = dataSource.findAllTitle();
                mAdapter.notifyDataSetChanged();
                displayList();
            }
        }
    }

    @Override
    public void onRecyclerViewItemClicked(int position, int id) {
        BookModel book = mData.get(position);
        ShowBookDetailDialogFragment dialogFragment = ShowBookDetailDialogFragment.getInstance(book);
        dialogFragment.show(getSupportFragmentManager(), "fragment");
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onFaveMeListener(int id, boolean isfaved) {
        String msg;

        if (isfaved) {
            dataSource.set_fav(id);
            msg = getString(R.string.book_faved);
        } else {
            dataSource.remove_faved(id);
            msg = getString(R.string.book_removed_faved);
        }

        if (isFavedIntent) {
            mData = dataSource.findAllfaved();
        } else {
            mData = dataSource.findAllTitle();
        }
        mAdapter.notifyDataSetChanged();
        displayList();
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        FlurryAgent.onEndSession(this);
        super.onStop();
    }
}
