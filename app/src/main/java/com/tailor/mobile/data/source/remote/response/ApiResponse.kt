package com.tailor.mobile.data.source.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Standard Laravel API response wrapper
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("meta")
    val meta: PaginationMeta? = null,

    // Specific to login response
    @SerializedName("token")
    val token: String? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    @SerializedName("user")
    val user: UserDto? = null,

    // Specific to validation error
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)

data class PaginationMeta(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Int
)

data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String
)
