package com.jimin.noteapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 메모 추가
    fun addNote(title: String, content: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
        }
        return db.insert(TABLE_NAME, null, values).also {
            db.close()
        }
    }

    // 모든 메모 조회
    fun getAllNotes(): List<Note> {
        val db = readableDatabase
        val notes = mutableListOf<Note>()
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_ID DESC")

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID))
                    val title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = c.getString(c.getColumnIndexOrThrow(COLUMN_CONTENT))
                    notes.add(Note(id, title, content))
                } while (c.moveToNext())
            }
        }
        db.close()
        return notes
    }

    // 메모 업데이트
    fun updateNote(id: Int, title: String, content: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }

    // 메모 삭제
    fun deleteNote(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }

    companion object {
        const val DATABASE_NAME = "notes.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
    }

    // title로 메모 검색!!!!!!!!
    fun searchByTitle(title: String): List<Note> {
        val db = readableDatabase
        val notes = mutableListOf<Note>()
        val cursor: Cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_TITLE LIKE ?",
            arrayOf("%$title%"),
            null,
            null,
            "$COLUMN_ID DESC"
        )

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID))
                    val noteTitle = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = c.getString(c.getColumnIndexOrThrow(COLUMN_CONTENT))
                    notes.add(Note(id, noteTitle, content))
                } while (c.moveToNext())
            }
        }
        db.close()
        return notes
    }

}
