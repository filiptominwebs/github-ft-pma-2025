// kotlin
package com.example.cviko012noteapp.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object NoteHubDatabaseInstance {

    @Volatile
    private var INSTANCE: NoteHubDatabase? = null

    // Bezpečná migrace 1 -> 2: vytvoří novou tabulku s createdAt a category, zkopíruje základní data a smaže starou tabulku.
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Přejmenuj starou tabulku (bez ztráty dat)
            db.execSQL("ALTER TABLE note_table RENAME TO note_table_old")

            // Vytvoř novou tabulku s požadovanou strukturou pro verzi 2
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS note_table (
                  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                  title TEXT NOT NULL,
                  content TEXT NOT NULL,
                  createdAt INTEGER NOT NULL DEFAULT 0,
                  category TEXT NOT NULL DEFAULT 'General'
                )
                """.trimIndent()
            )

            // Zkopíruj existující sloupce; nové sloupce naplnit výchozími hodnotami
            db.execSQL(
                """
                INSERT INTO note_table (id, title, content, createdAt, category)
                SELECT id, title, content, 0, 'General' FROM note_table_old
                """.trimIndent()
            )

            // Odstraň starou tabulku
            db.execSQL("DROP TABLE IF EXISTS note_table_old")
        }
    }

    // Robustní migrace 2 -> 3: odstraní sloupec 'color' (pokud existuje) a opraví název createdAT -> createdAt
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Přejmenuj starou tabulku
            db.execSQL("ALTER TABLE note_table RENAME TO note_table_old")

            // Vytvoř novou cílovou tabulku (verze 3) bez sloupce color
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS note_table (
                  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                  title TEXT NOT NULL,
                  content TEXT NOT NULL,
                  createdAt INTEGER NOT NULL DEFAULT 0,
                  category TEXT NOT NULL DEFAULT 'General'
                )
                """.trimIndent()
            )

            // Zjisti skutečné názvy sloupců ve staré tabulce
            val cursor = db.query("PRAGMA table_info('note_table_old')")
            val existingColumns = mutableSetOf<String>()
            cursor.use {
                while (it.moveToNext()) {
                    val colName = it.getString(it.getColumnIndexOrThrow("name"))
                    existingColumns.add(colName)
                }
            }

            // Urči zdroj pro createdAt a category
            val createdAtSource = when {
                existingColumns.contains("createdAt") -> "createdAt"
                existingColumns.contains("createdAT") -> "createdAT"
                else -> "0"
            }
            val categorySource = if (existingColumns.contains("category")) "category" else "'General'"

            // Zkopíruj data z původní tabulky; ignoruj color
            val insertSql = """
                INSERT INTO note_table (id, title, content, createdAt, category)
                SELECT id, title, content, $createdAtSource, $categorySource FROM note_table_old
            """.trimIndent()

            db.execSQL(insertSql)

            // Odstraň starou tabulku
            db.execSQL("DROP TABLE IF EXISTS note_table_old")
        }
    }

    // Robustní migrace 1 -> 3: detekuje, zda stará tabulka má sloupec 'createdAT' nebo 'createdAt' a zda má 'category' nebo 'color',
    // a vytvoří novou tabulku ve tvaru, který očekává entita (bez sloupce 'color' a se správným 'createdAt')
    private val MIGRATION_1_3 = object : Migration(1, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Pokud tabulka neexistuje, nic dělat
            // Přejmenuj starou tabulku
            db.execSQL("ALTER TABLE note_table RENAME TO note_table_old")

            // Vytvoř novou tabulku s cílovou strukturou (verze 3 entitě bez 'color')
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS note_table (
                  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                  title TEXT NOT NULL,
                  content TEXT NOT NULL,
                  createdAt INTEGER NOT NULL DEFAULT 0,
                  category TEXT NOT NULL DEFAULT 'General'
                )
                """.trimIndent()
            )

            // Zjisti skutečné názvy sloupců ve staré tabulce
            val cursor = db.query("PRAGMA table_info('note_table_old')")
            val existingColumns = mutableSetOf<String>()
            cursor.use {
                while (it.moveToNext()) {
                    val colName = it.getString(it.getColumnIndexOrThrow("name"))
                    existingColumns.add(colName)
                }
            }

            // Urči zdroj pro createdAt a category
            val createdAtSource = when {
                existingColumns.contains("createdAt") -> "createdAt"
                existingColumns.contains("createdAT") -> "createdAT"
                else -> "0"
            }
            val categorySource = if (existingColumns.contains("category")) "category" else "'General'"

            // Zkopíruj data z původní tabulky. Ignoruj sloupec 'color', pokud existuje.
            val insertSql = """
                INSERT INTO note_table (id, title, content, createdAt, category)
                SELECT id, title, content, $createdAtSource, $categorySource FROM note_table_old
            """.trimIndent()

            db.execSQL(insertSql)

            // Odstraň starou tabulku
            db.execSQL("DROP TABLE IF EXISTS note_table_old")
        }
    }

    fun getDatabase(context: Context): NoteHubDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteHubDatabase::class.java,
                "notehub_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
