package com.sostudio.apptemplate.di

import androidx.room.Room
import com.sostudio.apptemplate.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Room local cache — one database + per-entity DAOs, following the pattern in AppDatabase.kt's
 *  doc comment: bump `version` in AppDatabase every time an `@Entity` changes. */
val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "apptemplate.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<AppDatabase>().userDao() }
}
