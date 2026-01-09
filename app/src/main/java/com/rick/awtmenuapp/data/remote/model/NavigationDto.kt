package com.rick.awtmenuapp.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NavigationResponse(
    @Json(name = "result") val resultDto: ResultDto?,
    @Json(name = "session_id") val sessionId: String
)

@JsonClass(generateAdapter = true)
data class MenuItemDto(
    @Json(name = "label") val label: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "type") val type: Int?,
    @Json(name = "class") val className: String?
)

@JsonClass(generateAdapter = true)
data class ResultDto(
    @Json(name = "title") val title: String?,
    @Json(name = "user_photo") val userPhoto: String?,
    @Json(name = "cover_photo") val coverPhoto: String?,
    @Json(name = "wallet_amount") val walletAmount: String?,
    @Json(name = "wallet_url") val walletUrl: String?,
    @Json(name = "menus") val menus: List<MenuItemDto>?,
    @Json(name = "notification_count") val notificationCount: Long?,
    @Json(name = "friend_req_count") val friendReqCount: Long?,
    @Json(name = "message_count") val messageCount: Long?,
    @Json(name = "loggedin_user_id") val loggedinUserId: Long?,
)