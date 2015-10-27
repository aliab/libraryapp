package info.abdolahi.libraryapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.List;


public class TextCellDataSource {

    DataBaseHelper dbhelper;
    SQLiteDatabase database;


    public TextCellDataSource(Context context) {
        dbhelper = new DataBaseHelper(context);
        database = DataBaseHelper.getDatabase();
    }

    public void open() {

        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        database = DataBaseHelper.getDatabase();
    }

    public void close() {
        dbhelper.close();
        database.close();
    }

    /**
     * get all books object by its title
     *
     * @return
     */
    public List<BookModel> findAllTitle() {
        open();
        Cursor cursor = database.rawQuery("select * from books order by _id",
                null);

        List<BookModel> books = DataBaseHelper.returnBooksList(cursor);
        close();
        cursor.close();
        return books;
    }

    /**
     * get all categories
     *
     * @return
     */
    public List<CategoryModel> findAllCategory() {
        open();
        Cursor cursor = database.rawQuery("select * from categories order by _id",
                null);

        List<CategoryModel> categories = DataBaseHelper.returnCaregoriesList(cursor);
        close();
        cursor.close();
        return categories;
    }

    /**
     * get all books object by publisher
     *
     * @return
     */
    public List<BookModel> findAllPublisherBooks(String publisher) {
        open();
        Cursor cursor = database.rawQuery("select * from books WHERE " + DataBaseHelper.PUBLISHER + "=" + publisher + " order by _id",
                null);

        List<BookModel> books = DataBaseHelper.returnBooksList(cursor);
        close();
        cursor.close();
        return books;
    }

    /**
     * find all books with is_faved = 1
     *
     * @return
     */
    public List<BookModel> findAllfaved() {
        open();
        Cursor cursor = database.rawQuery(
                "select * from books WHERE is_faved = 1 order by _id", null);
        List<BookModel> books = DataBaseHelper.returnBooksList(cursor);
        close();
        cursor.close();
        return books;

    }

    /**
     * get list pair list of fieldsName/fieldsValue to search in db
     *
     * @param fields
     * @return
     */
    public List<BookModel> searchInDB(List<SearchModel> fields) {
        open();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from books WHERE ");
        for (int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i).fieldName + " LIKE '%" + fields.get(i).fieldValue + "%' ");
        }
        sb.append(" order by _id");

        Cursor cursor = database.rawQuery(sb.toString(), null);
        List<BookModel> books = DataBaseHelper.returnBooksList(cursor);
        close();
        cursor.close();
        return books;
    }

    /**
     * get list pair list of fieldsName/fieldsValue to search in db and is_faved=1
     *
     * @param fields
     * @return
     */
    public List<BookModel> searchInFavoriteDB(List<SearchModel> fields) {
        open();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from books WHERE ");
        for (int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i).fieldName + " LIKE '%" + fields.get(i).fieldValue + "%' ");
            if (fields.size() > 0 && i < (fields.size() - 1)) {
                sb.append(" AND ");
            }
        }
        sb.append(" AND is_faved = 1 order by _id");

        Cursor cursor = database.rawQuery(sb.toString(), null);
        List<BookModel> books = DataBaseHelper.returnBooksList(cursor);
        close();
        cursor.close();
        return books;
    }


    /**
     * find one book with specifc id
     *
     * @param id
     * @return
     */
    public BookModel findBook(int id) {
        open();
        Cursor cursor = database.rawQuery("select * from books where _id=" + id,
                null);

        BookModel book = null;

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                book = DataBaseHelper.setBookCurserIntoObject(cursor);
            }
        } else {
        }

        close();
        cursor.close();
        return book;

    }

    /**
     * favorite book
     *
     * @param id
     * @return
     */
    public Boolean set_fav(int id) {
        open();
        database.execSQL("UPDATE books SET is_faved=1 WHERE _id=" + id);
        close();
        return true;
    }

    /**
     * remove books from favorite list
     *
     * @param id
     * @return
     */
    public Boolean remove_faved(int id) {
        open();
        database.execSQL("UPDATE books SET is_faved=0 WHERE _id=" + id);
        close();
        return true;
    }

}
