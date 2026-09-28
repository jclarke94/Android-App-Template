package com.sostudio.apptemplate.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostudio.apptemplate.data.remote.model.response.UserResponse

/** A single-row cache assuming exactly one logged-in user, mirroring iOS's `UserEntity`
 *  (`UserStorageService`'s `fetchLimit = 1`, no predicate). */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val username: String?,
    val avatar: String?,
    val avatarUrl: String?,
    val dateOfBirth: String?,
    val emailVerifiedAt: String?,
    val bannedAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)

fun UserResponse.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    username = username,
    avatar = avatar,
    avatarUrl = avatarUrl,
    dateOfBirth = dateOfBirth,
    emailVerifiedAt = emailVerifiedAt,
    bannedAt = bannedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserEntity.toResponse() = UserResponse(
    id = id,
    name = name,
    email = email,
    username = username,
    avatar = avatar,
    avatarUrl = avatarUrl,
    dateOfBirth = dateOfBirth,
    emailVerifiedAt = emailVerifiedAt,
    bannedAt = bannedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
