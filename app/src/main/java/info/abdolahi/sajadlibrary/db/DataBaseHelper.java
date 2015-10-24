package info.abdolahi.sajadlibrary.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SdCardPath")
public class DataBaseHelper extends SQLiteOpenHelper {


    /**
     * DB FIELD KEY
     */
    public static String SARSHENASE = "sarshenase";
    public static String TITLE = "title";
    public static String PUBLISHER = "publisher";
    public static String MOTARJEM = "motarjem";
    public static String PUBLISH_YEAR = "publish_year";
    public static String ID = "_id";
    public static String CATEGORY_ID = "category_id";
    public static String IS_FAVED = "is_faved";

    /**
     * get one curser and return BookModel of that
     *
     * @param cursor
     * @return
     */
    public static BookModel setBookCurserIntoObject(Cursor cursor) {
        BookModel book = new BookModel();
        book.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ID)));
        book.setSarsehnase(cursor.getString(cursor.getColumnIndex(DataBaseHelper.SARSHENASE)));
        book.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TITLE)));
        book.setPublisher(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PUBLISHER)));
        book.setMotarjem(cursor.getString(cursor.getColumnIndex(DataBaseHelper.MOTARJEM)));
        book.setPublish_year(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PUBLISH_YEAR)));
        book.setCategory_id(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CATEGORY_ID)));
        book.setIs_faved(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.IS_FAVED)));
        return book;
    }

    public static CategoryModel setCategoryCurserIntoObject(Cursor cursor) {
        CategoryModel book = new CategoryModel();
        book.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ID)));
        book.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TITLE)));

        return book;
    }

    public static List<BookModel> returnBooksList(Cursor cursor) {
        List<BookModel> books = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                books.add(setBookCurserIntoObject(cursor));
            }
        }
        return books;
    }

    public static List<CategoryModel> returnCaregoriesList(Cursor cursor) {
        List<CategoryModel> categories = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                categories.add(setCategoryCurserIntoObject(cursor));
            }
        }
        return categories;
    }

    // The Android's default system path of your application database.
    private static String DB_PATH = "";//

    private static String DB_NAME = "sajad_library.db";

    public SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    public static SQLiteDatabase getDatabase() {
        String myPath = DB_PATH + DB_NAME;
        return SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the
    // database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd
    // be easy
    // to you to create adapters for your views.

}