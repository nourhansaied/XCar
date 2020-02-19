package com.victoria.customer.data.pojo;
import com.google.gson.annotations.SerializedName

class PigListing(@SerializedName("pig_listing")
                 var pigListing: ArrayList<PigData>,
                 @SerializedName("notification_count")
                 var notificationCount: Int) {
    class PigData(@SerializedName("pig_discussions")
                  var pigDiscussions: List<Discussion>? = null,
                  @SerializedName("is_favourite")
                  var isFavourite: Int = 0,
                  @SerializedName("is_pigu")
                  var isPigu: Int = 0,
                  @SerializedName("pig_tag")
                  var pigTag: String,
                  @SerializedName("profession")
                  var profession: String,
                  @SerializedName("updatetime")
                  var updatetime: String,
                  @SerializedName("insertdate")
                  var insertdate: String,
                  @SerializedName("is_active")
                  var isActive: String,
                  @SerializedName("longitude")
                  var longitude: String,
                  @SerializedName("latitude")
                  var latitude: String,
                  @SerializedName("discussion_count")
                  var discussionCount: String,
                  @SerializedName("pigu_count")
                  var piguCount: String,
                  @SerializedName("country")
                  var country: String,
                  @SerializedName("state")
                  var state: String,
                  @SerializedName("city")
                  var city: String,
                  @SerializedName("is_anonymously")
                  var isAnonymously: String,
                  @SerializedName("comment")
                  var comment: String,
                  @SerializedName("gender")
                  var gender: String,
                  @SerializedName("location")
                  var location: String,
                  @SerializedName("name")
                  var name: String,
                  @SerializedName("pig_image")
                  var pigImage: String,
                  @SerializedName("cover_pigimage")
                  var coverPigImage: String,
                  @SerializedName("pig_tag_id")
                  var pigTagId: String,
                  @SerializedName("user_id")
                  var userId: String,
                  @SerializedName("id")
                  var id: String,
                  @SerializedName("position")
                  var position: String,
                  @SerializedName("area")
                  var area: String,
                  @SerializedName("contest_prize")
                  var contestPrize: String,
                  @SerializedName("contest_name")
                  var contestName: String,
                  @SerializedName("share_link")
                  var shareLink: String)

    class Discussion(@SerializedName("profile_image")
                     var profileImage: String,
                     @SerializedName("user_name")
                     var userName: String,
                     @SerializedName("updatetime")
                     var updatetime: String,
                     @SerializedName("insertdate")
                     var insertdate: String,
                     @SerializedName("is_active")
                     var isActive: String,
                     @SerializedName("discussion")
                     var discussion: String,
                     @SerializedName("user_id")
                     var userId: String,
                     @SerializedName("pig_id")
                     var pigId: String,
                     @SerializedName("id")
                     var id: String,
                     @SerializedName("is_block")
                     var isBlock: String)
}