package com.example.ehcf.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager

class SessionManager(context: Context?) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        private const val IS_LOGIN = "islogin"
        private const val HOSPITAL_ID = "hospital_id"
        private const val UNIQUE_CODE="unique_code"
        private const val CREATED_AT="created_at"
        private const val DOCTOR_NAME="doctor_name"
        private const val QUALIFICATION="qualification"
        private const val EMAIL="email"
        private const val FCM_TOKEN="fcm_token"
        private const val GENDER="gender"
        private const val LATITUDE="latitude"
        private const val LONGITUDE="longitude"
        private const val ID="id"
        private const val C_ID="c_id"
        private const val S_STAT="c_stat"
        private const val EARNINGS="earnings"
        private const val EXPERIENCE="experience"
        private const val DESCRIPTION="description"
        private const val IS_RECOMMENDED="is_recommended"
        private const val BOOKING_COMMISSION="booking_commission"
        private const val NO_OF_RATINGS="no_of_ratings"
        private const val OVERALL_RATING="overall_ratings"
        private const val PASSWORD="password"
        private const val PHONE_NUMBER="phone_number"
        private const val PHONE_WITH_CODE="phone_with_code"
        private const val PRE_EXISTING_DESEASE="pre_existing_desease"
        private const val PROFILE_PICTURE="profile_picture"
        private const val STATUS="status"
        private const val UPDATED_AT="updated_at"
        private const val WALLET="wallet"
    }

    var isLogin: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(isLogin) {
            prefs.edit().putBoolean(IS_LOGIN, isLogin).apply()
        }

    fun logout() {
        prefs.edit().clear().apply()
    }

    var fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, "")
        set(fcmToken) {
            prefs.edit().putString(FCM_TOKEN, fcmToken).apply()
        }
    var password: String?
        get() = prefs.getString(PASSWORD, "")
        set(password) {
            prefs.edit().putString(PASSWORD, password).apply()
        }
    var experience: String?
        get() = prefs.getString(EXPERIENCE, "")
        set(experience) {
            prefs.edit().putString(EXPERIENCE, experience).apply()
        }
    var cID: String?
        get() = prefs.getString(C_ID, "")
        set(cID) {
            prefs.edit().putString(C_ID, cID).apply()
        }
    var sStat: String?
        get() = prefs.getString(S_STAT, "")
        set(sStat) {
            prefs.edit().putString(S_STAT, sStat).apply()
        }
    var status: String?
        get() = prefs.getString(STATUS, "")
        set(status) {
            prefs.edit().putString(S_STAT, status).apply()
        }
    var qualification: String?
        get() = prefs.getString(QUALIFICATION, "")
        set(qualification) {
            prefs.edit().putString(QUALIFICATION, qualification).apply()
        }
    var  hospitalID: String?
        get() = prefs.getString(HOSPITAL_ID, "")
        set(hospitalID) {
            prefs.edit().putString(HOSPITAL_ID, hospitalID).apply()
        }
    var  uniqueCode: String?
        get() = prefs.getString(UNIQUE_CODE, "")
        set(uniqueCode) {
            prefs.edit().putString(UNIQUE_CODE, uniqueCode).apply()
        }
    var  doctorName: String?
        get() = prefs.getString(DOCTOR_NAME, "")
        set(doctorName) {
            prefs.edit().putString(DOCTOR_NAME, doctorName).apply()
        }
    var  email: String?
        get() = prefs.getString(EMAIL, "")
        set(email) {
            prefs.edit().putString(EMAIL, email).apply()
        }

    var  phoneNumber: String?
        get() = prefs.getString(PHONE_NUMBER, "")
        set(phoneNumber) {
            prefs.edit().putString(PHONE_NUMBER, phoneNumber).apply()
        }
    var  phoneWithCode: String?
        get() = prefs.getString(PHONE_WITH_CODE, "")
        set(phoneWithCode) {
            prefs.edit().putString(PHONE_WITH_CODE, phoneWithCode).apply()
        }

    var id: Int
        get() = prefs.getInt(ID, -1)
        set(id) {
                prefs.edit().putInt(ID, id).apply()
        }

    var gender: String?
        get() = prefs.getString(GENDER, "")
        set(gender) {
            prefs.edit().putString(GENDER, gender).apply()
        }
    var latitude: String?
        get() = prefs.getString(LATITUDE, "")
        set(latitude) {
            prefs.edit().putString(LATITUDE, latitude).apply()
        }
    var longitude: String?
        get() = prefs.getString(LONGITUDE, "")
        set(longitude) {
            prefs.edit().putString(LONGITUDE, longitude).apply()
        }

}