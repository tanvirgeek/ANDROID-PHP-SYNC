package com.tanvirgeek.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "+DBContact.TABLE_NAME+" (id integer primary key autoincrement, " +DBContact.COL_NAME+
            " text, " +DBContact.COL_SYNC_STATUS+" integer);";

    private static final String DROP_TABLE = "drop table if exists "+ DBContact.TABLE_NAME;
    public DbHelper(Context context){
        super(context,DBContact.DATABASE_NAME,null,DATABASE_VERSION);
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean saveToLocalDatabase(String name, int syncStatus, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContact.COL_NAME, name);
        contentValues.put(DBContact.COL_SYNC_STATUS,syncStatus);
        Long t = db.insert(DBContact.TABLE_NAME,null,contentValues);
        if(t == -1){
            return false;
        }else{
            return true;
        }


    }

    public Cursor readfromLocalDatabase(SQLiteDatabase database ){
        String [] projection = {DBContact.COL_NAME,DBContact.COL_SYNC_STATUS};
        return (database.query(DBContact.TABLE_NAME,projection,null,null,null,null,null));
    }

    public void updateLocalDatabase(String name, int syncStatus, SQLiteDatabase db){
        ContentValues c = new ContentValues();
        c.put(DBContact.COL_SYNC_STATUS,syncStatus);
        String selection = DBContact.COL_NAME + "LIKE ?";
        String[] selectionArgs = {name};
        db.update(DBContact.TABLE_NAME,c,selection,selectionArgs);
    }
}
