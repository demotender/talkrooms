package com.vangood.chatfrag0315

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ChatRooms(
    val error_code: String,
    val error_text: String,
    val result: Result
)

data class Result(
    val lightyear_list: List<Lightyear>,
    val stream_list: List<Stream>
)
@Parcelize
data class Lightyear(
    val background_image: String,
    val charge: Int,
    val closed_at: Int,
    val deleted_at: Int,
    val game: String,
    val group_id: Int,
    val head_photo: String,
    val nickname: String,
    val online_num: Int,
    val open_at: Int,
    val open_attention: Boolean,
    val open_guardians: Boolean,
    val start_time: Int,
    val status: Int,
    val stream_id: Int,
    val stream_title: String,
    val streamer_id: Int,
    val tags: String
):Parcelable

data class Stream(
    val background_image: String,
    val charge: Int,
    val closed_at: Int,
    val deleted_at: Int,
    val game: String,
    val group_id: Int,
    val head_photo: String,
    val nickname: String,
    val online_num: Int,
    val open_at: Int,
    val open_attention: Boolean,
    val open_guardians: Boolean,
    val start_time: Int,
    val status: Int,
    val stream_id: Int,
    val stream_title: String,
    val streamer_id: Int,
    val tags: String
)

//一般發話 [default_message]
data class DefaultMessage(
    val body: Body,
    val event: String,
    val room_id: String,
    val sender_role: Int,
    val time: String
)

data class Body(
    val accept_time: String,
    val account: String,
    val chat_id: String,
    val info: Info,
    val nickname: String,
    val recipient: String,
    val text: String,
    val type: String
)

data class Info(
    val badges: Any,
    val is_ban: Int,
    val is_guardian: Int,
    val last_login: Int,
    val level: Int
)

//進出更新通知 [sys_updateRoomStatus]
data class SysUpdateRoomStatus(
    val body: UpdateBody,
    val event: String,
    val room_id: String,
    val sender_role: Int,
    val time: String
)

data class UpdateBody(
    val contribute_sum: Int,
    val entry_notice: EntryNotice,
    val guardian_count: Int,
    val guardian_sum: Int,
    val real_count: Int,
    val room_count: Int,
    val user_infos: UserInfos
)

data class EntryNotice(
    val action: String,
    val entry_banner: EntryBanner,
    val head_photo: String,
    val username: String
)

data class UserInfos(
    val guardianlist: List<Any>,
    val onlinelist: Any
)

data class EntryBanner(
    val img_url: String,
    val main_badge: String,
    val other_badges: List<Any>,
    val present_type: String
)

//系統廣播 [admin_all_broadcast]
data class AdminInAllBroadcast(
    val body: BroadcastBody,
    val event: String,
    val room_id: String,
    val sender_role: Int,
    val time: String
)

data class BroadcastBody(
    val content: Content
)

data class Content(
    val cn: String,
    val en: String,
    val tw: String
)

//房間關閉 [sys_room_endStream]
data class SysRoomEndStream(
    val body: Body,
    val event: String,
    val room_id: String,
    val sender_role: Int,
    val time: String
)
