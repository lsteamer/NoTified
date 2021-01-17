package com.example.notified.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContentProviderDataBase(context: Context?) : SQLiteOpenHelper(context, "NODB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE notification_table(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "package_name TEXT, " +
                "group_key TEXT," +
                "key TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) = Unit
}