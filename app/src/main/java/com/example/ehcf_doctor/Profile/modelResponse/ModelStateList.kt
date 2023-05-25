package com.example.ehcf_doctor.Profile.modelResponse

import com.google.gson.annotations.SerializedName

data class ModelStateList(
    @SerializedName("key")  val key: String,
    @SerializedName("name") val name: String,
    )
{
    override fun toString(): String {
        return name
    }
}