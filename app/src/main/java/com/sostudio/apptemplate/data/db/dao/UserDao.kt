package com.sostudio.apptemplate.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sostudio.apptemplate.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /** No predicate, matches iOS's `fetchLimit = 1` — assumes exactly one logged-in user. */
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    /** Same row, as a stream — lets a top bar's account icon react to an avatar change. */
    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Upsert
    suspend fun upsert(user: UserEntity)
}
