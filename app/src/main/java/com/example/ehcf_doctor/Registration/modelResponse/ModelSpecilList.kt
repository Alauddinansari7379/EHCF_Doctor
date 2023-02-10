package com.example.ehcf_doctor.Registration.modelResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelSpecilList {
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    inner class Result {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("category_image")
        @Expose
        var categoryImage: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}