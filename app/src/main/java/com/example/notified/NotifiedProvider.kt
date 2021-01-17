package com.example.notified

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.example.notified.data.ContentProviderDataBase

class NotifiedProvider : ContentProvider() {

    lateinit var db: SQLiteDatabase

    companion object {
        val PROVIDER_NAME = "com.example.notified.data/NotifiedProvider"
        val URL = "content://$PROVIDER_NAME/notification_table"
        val CONTENT_URI = Uri.parse(URL)

        val _ID = "_id"
        val PACKAGE_NAME = "package_name"
        val GROUP_KEY = "group_key"
        val KEY = "key"
    }

    override fun onCreate(): Boolean {
        val dataBaseHelper = ContentProviderDataBase(context)
        db = dataBaseHelper.writableDatabase
        return true
    }

    override fun insert(uri: Uri, cv: ContentValues?): Uri {
        db.insert("notification_table", null, cv)
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun update(
        uri: Uri,
        cv: ContentValues?,
        condition: String?,
        conditionValues: Array<out String>?
    ): Int {
        val count = db.update("notification_table", cv, condition, conditionValues)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, condition: String?, conditionValues: Array<out String>?): Int {
        val count = db.delete("notification_table", condition, conditionValues)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun query(
        uri: Uri,
        colums: Array<out String>?,
        condition: String?,
        conditionValues: Array<out String>?,
        order: String?
    ): Cursor? {
        return db.query("notification_table", colums, condition, conditionValues, null, null, order)
    }

    override fun getType(p0: Uri): String {
        return "vnd.android.cursor.dir/vnd.lsteamer.notifitable"
    }

}