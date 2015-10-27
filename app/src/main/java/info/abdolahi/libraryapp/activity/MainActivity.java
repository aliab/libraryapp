package info.abdolahi.libraryapp.activity;

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

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.abdolahi.libraryapp.R;
import info.abdolahi.libraryapp.adapter.BooksListAdapter;
import info.abdolahi.libraryapp.db.BookModel;
import info.abdolahi.libraryapp.db.DataBaseHelper;
import info.abdolahi.libraryapp.db.SearchModel;
import info.abdolahi.libraryapp.db.TextCellDataSource;
import info.abdolahi.libraryapp.dialog.ShowBookDetailDialogFragment;

public class MainActivity extends AppCompatActivity implements BooksListAdapter.OnRecyclerViewItemClickListener, ShowBookDetailDialogFragment.faveMeDialogListener {

    //region INJECT VIEW
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
    //endregion

    //region GLOBAL VARIABLES
    TextCellDataSource dataSource;
    BooksListAdapter mAdapter;
    List<BookModel> mData;
    boolean isFavedIntent = false; // use this for knowing active activity state
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /**
         * set activity state boolean
         */
        if (getIntent().hasExtra("isfaved")) {
            isFavedIntent = true;
        }

        init_app();
        init_view();
    }

    /**
     * Base on what activity state we are in, return different data
     * @param withRefresh
     */
    private void getDefaultDisplayData(boolean withRefresh) {

        if (!isFavedIntent) {
            mData = dataSource.findAllTitle();
        } else {
            mData = dataSource.findAllfaved();
        }

        if (withRefresh) {
            mAdapter.notifyDataSetChanged();
            displayList();
        }
    }

    /**
     * Base on what activity state we are in, return different search data
     * @param searchModels
     * @param withRefresh
     */
    private void getDefaultDisplayData(List<SearchModel> searchModels, boolean withRefresh) {

        if (isFavedIntent) {
            mData = dataSource.searchInFavoriteDB(searchModels);
        } else {
            mData = dataSource.searchInDB(searchModels);
        }
        if (withRefresh) {
            mAdapter.notifyDataSetChanged();
            displayList();
        }
    }

    /**
     * this is for implementing Recyclerview empty view, sry, google don't support empty view for Recyclerview
     */
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

        // change toolbar title, if we are in favorite state
        if (isFavedIntent) {
            toolbar_title.setText(getString(R.string.action_favorites));
        }

        /**
         * congifuration for Recyclerview
         */
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        /**
         *  for live search
         */
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic for on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                /**
                 * simply we search in db and update RecyclerView
                 */
                List<SearchModel> searchModels = new ArrayList<>();
                SearchModel searchModel = new SearchModel(); // Create Search field model
                searchModel.setFieldName(DataBaseHelper.TITLE); // set title, cause we just want to search in titles
                searchModel.setFieldValue(newText); // set search params
                searchModels.add(searchModel);
                getDefaultDisplayData(searchModels, true); // request to update display with data's
                return true;
            }
        });

        /**
         * in every state we request for getting data and setting into list for the first start of activity
         */
        displayList();
    }


    /**
     * get data from database and set it to list
     */
    private void displayList() {

        mAdapter = new BooksListAdapter(mData);
        mAdapter.setOnItemClickListener(this);

        /**
         * this part we observe RecyclerView for any change, if dataset is empty, so, we show empty image to user
         */
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });

        rv.setAdapter(mAdapter);

        // call check for empty state for first time
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

        // init datasource helper class
        dataSource = new TextCellDataSource(this);

        // we just want to get only data and set it to mData at this stage
        getDefaultDisplayData(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //set menu search item to searchview
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        // handle favorite state menu
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

        /**
         * for about us dialog
         */
        if (id == R.id.action_aboutus) {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle(getString(R.string.app_name) + " " + getString(R.string.app_version));

            StringBuilder sb = new StringBuilder();
            sb.append("ساخته شده در دپارتمان صنایع دانشگاه سجاد");
            sb.append("\n");
            sb.append("توسط علی عبداللهی");
            sb.append("\n");
            sb.append("پاییز ۱۳۹۴");

            dialog.setMessage(getString(R.string.about_dialog));
            dialog.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog.create().show();

            return true;

            /**
             * for show favorite, we rePopulate this activity with another state
             */
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
            // if we return from another state, we just want to update data states
            getDefaultDisplayData(true);
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
        getDefaultDisplayData(true);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();

    }

}
