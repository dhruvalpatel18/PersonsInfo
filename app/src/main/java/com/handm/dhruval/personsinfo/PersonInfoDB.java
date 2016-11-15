package com.handm.dhruval.personsinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.handm.dhruval.personsinfo.model.PersonInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruval on 11/14/2016.
 */

public class PersonInfoDB extends SQLiteOpenHelper {

    private static final String PERSONINFO_DATABASE = "person_info_db";
    private static final String PERSONINFO_TABLE = "person_info";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = PersonInfoDB.class.getSimpleName();

    public PersonInfoDB(Context context) {
        super(context, PERSONINFO_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PERSONINFO_TABLE + " ( " + FIRST_NAME + " TEXT not null, " + LAST_NAME + " TEXT not null" + ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person_info");
        onCreate(db);
    }

    public void onInsert(String firstname, String lastname) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_NAME, firstname);
        contentValues.put(LAST_NAME, lastname);
        long l = db.insert(PERSONINFO_TABLE, null, contentValues);
        if (l == -1) {
            Log.i(LOG_TAG, "Database Insertion Problem");
        }
    }

    public List<PersonInfo> getPersonInfoFromDB() {
        List<PersonInfo> personInfoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PERSONINFO_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name1 = cursor.getString(cursor.getColumnIndex(FIRST_NAME));
                String name2 = cursor.getString(cursor.getColumnIndex(LAST_NAME));

                PersonInfo personInfo = new PersonInfo(name1, name2);
                personInfoList.add(personInfo);

            } while(cursor.moveToNext());
            cursor.close();
        }
        return personInfoList;
    }
}
