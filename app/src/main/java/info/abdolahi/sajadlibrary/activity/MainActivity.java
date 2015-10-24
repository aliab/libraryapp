package info.abdolahi.sajadlibrary.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.abdolahi.sajadlibrary.BaseActivity;
import info.abdolahi.sajadlibrary.R;
import info.abdolahi.sajadlibrary.adapter.BooksListAdapter;
import info.abdolahi.sajadlibrary.db.BookModel;
import info.abdolahi.sajadlibrary.db.DataBaseHelper;
import info.abdolahi.sajadlibrary.db.TextCellDataSource;

public class MainActivity extends BaseActivity implements BooksListAdapter.OnRecyclerViewItemClickListener {

    @Bind(R.id.rv)
    RecyclerView rv;


    TextCellDataSource dataSource;
    BooksListAdapter mAdapter;
    List<BookModel> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init_app();
        init_view();
    }


    /**
     * init view config
     */
    private void init_view() {
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        /**
         * request for getting data and setting into list
         */
        displayList();
    }

    /**
     * get data from database and set it to list
     */
    private void displayList() {
        mData = dataSource.findAllTitle();
        mAdapter = new BooksListAdapter(mData);
        mAdapter.setOnItemClickListener(this);
        rv.setAdapter(mAdapter);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerViewItemClicked(int position, int id) {

    }
}
