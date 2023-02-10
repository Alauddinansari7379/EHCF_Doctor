package com.example.ehcf_doctor.Registration.modelResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelLanguage {
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

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

        @SerializedName("name")
        @Expose
        var name: String? = null


    }
}