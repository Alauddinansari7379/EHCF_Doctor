package com.example.ehcf_doctor.MyPatient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultNew {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("phone_with_code")
    @Expose
    private String phoneWithCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("pre_existing_desease")
    @Expose
    private Object preExistingDesease;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("overall_ratings")
    @Expose
    private String overallRatings;
    @SerializedName("no_of_ratings")
    @Expose
    private String noOfRatings;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("last_active_address")
    @Expose
    private String lastActiveAddress;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("report_1")
    @Expose
    private String report1;
    @SerializedName("report_2")
    @Expose
    private Object report2;
    @SerializedName("report_3")
    @Expose
    private Object report3;
    @SerializedName("report_4")
    @Expose
    private Object report4;
    @SerializedName("report_5")
    @Expose
    private Object report5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneWithCode() {
        return phoneWithCode;
    }

    public void setPhoneWithCode(String phoneWithCode) {
        this.phoneWithCode = phoneWithCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Object getPreExistingDesease() {
        return preExistingDesease;
    }

    public void setPreExistingDesease(Object preExistingDesease) {
        this.preExistingDesease = preExistingDesease;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getOverallRatings() {
        return overallRatings;
    }

    public void setOverallRatings(String overallRatings) {
        this.overallRatings = overallRatings;
    }

    public String getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(String noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getLastActiveAddress() {
        return lastActiveAddress;
    }

    public void setLastActiveAddress(String lastActiveAddress) {
        this.lastActiveAddress = lastActiveAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReport1() {
        return report1;
    }

    public void setReport1(String report1) {
        this.report1 = report1;
    }

    public String getReport2() {
        return (String) report2;
    }

    public void setReport2(String report2) {
        this.report2 = report2;
    }

    public Object getReport3() {
        return report3;
    }

    public void setReport3(Object report3) {
        this.report3 = report3;
    }

    public Object getReport4() {
        return report4;
    }

    public void setReport4(Object report4) {
        this.report4 = report4;
    }

    public Object getReport5() {
        return report5;
    }

    public void setReport5(Object report5) {
        this.report5 = report5;
    }

}
