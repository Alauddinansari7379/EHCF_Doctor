package com.example.ehcf_doctor.Profile.modelResponse

import com.google.gson.annotations.SerializedName

data class ModelCityList(
    @SerializedName("id")  val id: String,
    @SerializedName("name") val name: String,
    )
{
    override fun toString(): String {
        return name
    }
}