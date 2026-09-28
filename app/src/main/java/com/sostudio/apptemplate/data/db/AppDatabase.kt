package com.sostudio.apptemplate.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sostudio.apptemplate.data.db.dao.UserDao
import com.sostudio.apptemplate.data.db.entity.UserEntity

/**
 * Local cache — one database + one DAO per entity, mirroring iOS's single bespoke
 * `PersistenceController` rather than inventing a generic shared-repo abstraction iOS itself
 * doesn't have. No real migrations defined — `fallbackToDestructiveMigration` in
 * `DatabaseModule` covers schema changes, but only fires on an actual version *mismatch*; the
 * version number itself must still be bumped every time an `@Entity` changes — Room's
 * identity-hash check fails hard otherwise, even though the fallback is configured.
 *
 * `RoomDatabase.clearAllTables()` (inherited, no override needed) must be called alongside
 * [com.sostudio.apptemplate.data.prefs.PrefsHelper.onLogout] on every logout path — see
 * [performLogout].
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
