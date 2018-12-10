package enib.gala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper
{
    //information of database

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "userDB.db";

    private static final String TABLE_NAME = "User";

    private static final String COLUMN_ID = "userID";
    private static final String COLUMN_EMAIL = "userEmail";
    private static final String COLUMN_PASSWORD = "userPassword";
    private static final String COLUMN_TOKEN = "userToken";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID +

                " INTEGER PRIMARY KEY, " + COLUMN_EMAIL + " TEXT,"+ COLUMN_PASSWORD + " TEXT," + COLUMN_TOKEN+ " TEXT)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {

    }

    public String loadHandler()
    {
        String result = "";

        String query = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {

            int result_0 = cursor.getInt(0);

            String result_1 = cursor.getString(1);

            result += String.valueOf(result_0) + " " + result_1 +

                    System.getProperty("line.separator");

        }

        cursor.close();

        db.close();

        return result;
    }

    public void addHandler(User user)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, user.getId());

        values.put(COLUMN_EMAIL, user.getEmail());

        values.put(COLUMN_PASSWORD, user.getPassword());

        values.put(COLUMN_TOKEN, user.getToken());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public User findHandler(String userEmail)
    {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = " + "'" + userEmail + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user;

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();

            user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));

            cursor.close();

        } else {

            user = null;

        }

        db.close();

        return user;
    }

    public User findFirstHandler()
    {
        String query = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user;

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();

            user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));

            cursor.close();

        } else {

            user = new User(null,null,null,null);

        }

        db.close();

        return user;
    }

    public boolean deleteHandler(int ID)
    {
        boolean result = false;

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "= '" + String.valueOf(ID) + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user;

        if (cursor.moveToFirst()) {

            user = new User(Integer.parseInt(cursor.getString(0)),null,null,null);


            db.delete(TABLE_NAME, COLUMN_ID + "=?",

                    new String[] {

                String.valueOf(user.getId())

            });

            cursor.close();

            result = true;

        }

        db.close();

        return result;
    }

    public boolean updateHandler(int ID, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(COLUMN_ID, ID);

        args.put(COLUMN_EMAIL, email);

        args.put(COLUMN_PASSWORD, password);

        return db.update(TABLE_NAME, args, COLUMN_ID + "=" + ID, null) > 0;
    }

    public void emptyTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
        db.close();
    }

}
