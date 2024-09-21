package com.example.ehcf.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager
import retrofit2.http.POST

class SessionManager(context: Context?) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        private const val IS_LOGIN = "islogin"
        private const val HOSPITAL_ID = "hospital_id"
        private const val UNIQUE_CODE = "unique_code"
        private const val CREATED_AT = "created_at"
        private const val DOCTOR_NAME = "doctor_name"
        private const val QUALIFICATION = "qualification"
        private const val EMAIL = "email"
        private const val FCM_TOKEN = "fcm_token"
        private const val GENDER = "gender"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val ID = "id"
        private const val C_ID = "c_id"
        private const val S_STAT = "c_stat"
        private const val EARNINGS = "earnings"
        private const val EXPERIENCE = "experience"
        private const val DESCRIPTION = "description"
        private const val IS_RECOMMENDED = "is_recommended"
        private const val BOOKING_COMMISSION = "booking_commission"
        private const val NO_OF_RATINGS = "no_of_ratings"
        private const val OVERALL_RATING = "overall_ratings"
        private const val PASSWORD = "password"
        private const val PHONE_NUMBER = "phone_number"
        private const val PHONE_WITH_CODE = "phone_with_code"
        private const val PRE_EXISTING_DESEASE = "pre_existing_desease"
        private const val PROFILE_PICTURE = "profile_picture"
        private const val STATUS = "status"
        private const val ONLINE_STATUS = "online_status"
        private const val UPDATED_AT = "updated_at"
        private const val WALLET = "wallet"
        private const val CLINIC_ADDRESS = "clinic_address"
        private const val PRICING = "pricing"
        private const val CLINIC_NAME = "clinic_name"
        private const val ADDRESS = "address"
        private const val SERVICES = "services"
        private const val COLLEGE = "college"
        private const val HOSPITAL_NAME = "hos_name"
        private const val HOSPITAL_ADDRESS = "hos_address"
        private const val REGISTRATION = "registration"
         private const val SPECIALIST = "specialist"
         private const val YEAROFCOMPLETION = "yearOfCompletion"
         private const val REGISTRATIONYEAR = "registrationYear"
        private const val CLINIC_ADDRESS_ONE = "clinic_address_one"
        private const val CLINIC_ADDRESS_TWO = "clinic_address_two"
        private const val CITY = "city"
        private const val STATE = "state"
        private const val OPENTIME = "openTime"
        private const val CLOSETIME = "closeTime"
        private const val POSTALCODE = "postalCode"
        private const val IMAGEURL = "imageUrl"
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
    var clinicAddressOne: String?
        get() = prefs.getString(CLINIC_ADDRESS_ONE, "")
        set(clinicAddressOne) {
            prefs.edit().putString(CLINIC_ADDRESS_ONE, clinicAddressOne).apply()
        }
    var clinicAddressTwo: String?
        get() = prefs.getString(CLINIC_ADDRESS_TWO, "")
        set(clinicAddressTwo) {
            prefs.edit().putString(CLINIC_ADDRESS_TWO, clinicAddressTwo).apply()
        }
    var specialist: String?
        get() = prefs.getString(SPECIALIST, "")
        set(specialist) {
            prefs.edit().putString(SPECIALIST, specialist).apply()
        }

    var imageUrl: String?
        get() = prefs.getString(IMAGEURL, "")
        set(imageUrl) {
            prefs.edit().putString(IMAGEURL, imageUrl).apply()
        }

    var openTime: String?
        get() = prefs.getString(OPENTIME, "")
        set(openTime) {
            prefs.edit().putString(OPENTIME, openTime).apply()
        }

    var closeTime: String?
        get() = prefs.getString(CLOSETIME, "")
        set(closeTime) {
            prefs.edit().putString(CLOSETIME, closeTime).apply()
        }
    var postalCode: String?
        get() = prefs.getString(POSTALCODE, "")
        set(postalCode) {
            prefs.edit().putString(POSTALCODE, postalCode).apply()
        }

    var yearOfCompletion: String?
        get() = prefs.getString(YEAROFCOMPLETION, "")
        set(yearOfCompletion) {
            prefs.edit().putString(YEAROFCOMPLETION, yearOfCompletion).apply()
        }

    var registrationYear: String?
        get() = prefs.getString(REGISTRATIONYEAR, "")
        set(registrationYear) {
            prefs.edit().putString(REGISTRATIONYEAR, registrationYear).apply()
        }
    var city: String?
        get() = prefs.getString(CITY, "")
        set(city) {
            prefs.edit().putString(CITY, city).apply()
        }
    var state: String?
        get() = prefs.getString(STATE, "")
        set(state) {
            prefs.edit().putString(STATE, state).apply()
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
    var wallet: String?
        get() = prefs.getString(WALLET, "")
        set(wallet) {
            prefs.edit().putString(WALLET, wallet).apply()
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
            prefs.edit().putString(STATUS, status).apply()
        }

    var onlineStatus: String?
        get() = prefs.getString(ONLINE_STATUS, "")
        set(onlineStatus) {
            prefs.edit().putString(ONLINE_STATUS, onlineStatus).apply()
        }
    var qualification: String?
        get() = prefs.getString(QUALIFICATION, "")
        set(qualification) {
            prefs.edit().putString(QUALIFICATION, qualification).apply()
        }
    var hospitalID: String?
        get() = prefs.getString(HOSPITAL_ID, "")
        set(hospitalID) {
            prefs.edit().putString(HOSPITAL_ID, hospitalID).apply()
        }
    var uniqueCode: String?
        get() = prefs.getString(UNIQUE_CODE, "")
        set(uniqueCode) {
            prefs.edit().putString(UNIQUE_CODE, uniqueCode).apply()
        }
    var doctorName: String?
        get() = prefs.getString(DOCTOR_NAME, "")
        set(doctorName) {
            prefs.edit().putString(DOCTOR_NAME, doctorName).apply()
        }
    var email: String?
        get() = prefs.getString(EMAIL, "")
        set(email) {
            prefs.edit().putString(EMAIL, email).apply()
        }

    var phoneNumber: String?
        get() = prefs.getString(PHONE_NUMBER, "")
        set(phoneNumber) {
            prefs.edit().putString(PHONE_NUMBER, phoneNumber).apply()
        }
    var phoneWithCode: String?
        get() = prefs.getString(PHONE_WITH_CODE, "")
        set(phoneWithCode) {
            prefs.edit().putString(PHONE_WITH_CODE, phoneWithCode).apply()
        }
    var clinicAddress: String?
        get() = prefs.getString(CLINIC_ADDRESS, "null")
        set(clinicAddress) {
            prefs.edit().putString(CLINIC_ADDRESS, clinicAddress).apply()
        }
    var pricing: String?
        get() = prefs.getString(PRICING, "")
        set(pricing) {
            prefs.edit().putString(PRICING, pricing).apply()
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

    var clinicName: String?
        get() = prefs.getString(CLINIC_NAME, "")
        set(clinicName) {
            prefs.edit().putString(CLINIC_NAME, clinicName).apply()
        }
    var address: String?
        get() = prefs.getString(ADDRESS, "")
        set(address) {
            prefs.edit().putString(ADDRESS, address).apply()
        }
    var services: String?
        get() = prefs.getString(SERVICES, "")
        set(services) {
            prefs.edit().putString(SERVICES, services).apply()
        }
    var college: String?
        get() = prefs.getString(COLLEGE, "")
        set(college) {
            prefs.edit().putString(COLLEGE, college).apply()
        }
    var hospitalName: String?
        get() = prefs.getString(HOSPITAL_NAME, "")
        set(hospitalName) {
            prefs.edit().putString(HOSPITAL_NAME, hospitalName).apply()
        }
    var hospitalAddress: String?
        get() = prefs.getString(HOSPITAL_ADDRESS, "")
        set(hospitalAddress) {
            prefs.edit().putString(HOSPITAL_ADDRESS, hospitalAddress).apply()
        }
    var registration: String?
        get() = prefs.getString(REGISTRATION, "")
        set(registration) {
            prefs.edit().putString(REGISTRATION, registration).apply()
        }

    var profilePic: String?
        get() = prefs.getString(PROFILE_PICTURE, "")
        set(profilePic) {
            prefs.edit().putString(PROFILE_PICTURE, profilePic).apply()
        }

}