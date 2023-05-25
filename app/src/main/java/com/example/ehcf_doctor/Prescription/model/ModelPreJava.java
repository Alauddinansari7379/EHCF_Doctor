package com.example.ehcf_doctor.Prescription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPreJava {

    public class Data {

        @SerializedName("booking_id")
        @Expose
        private Integer bookingId;
        @SerializedName("doctor_id")
        @Expose
        private String doctorId;
        @SerializedName("patient_id")
        @Expose
        private String patientId;
        @SerializedName("subjective_information")
        @Expose
        private String subjectiveInformation;
        @SerializedName("objective_information")
        @Expose
        private String objectiveInformation;
        @SerializedName("assessment")
        @Expose
        private String assessment;
        @SerializedName("plan")
        @Expose
        private String plan;
        @SerializedName("is_test")
        @Expose
        private String isTest;
        @SerializedName("doctor_notes")
        @Expose
        private String doctorNotes;
        @SerializedName("test_name")
        @Expose
        private String testName;
        @SerializedName("instructions")
        @Expose
        private String instructions;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;

        public Integer getBookingId() {
            return bookingId;
        }

        public void setBookingId(Integer bookingId) {
            this.bookingId = bookingId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getSubjectiveInformation() {
            return subjectiveInformation;
        }

        public void setSubjectiveInformation(String subjectiveInformation) {
            this.subjectiveInformation = subjectiveInformation;
        }

        public String getObjectiveInformation() {
            return objectiveInformation;
        }

        public void setObjectiveInformation(String objectiveInformation) {
            this.objectiveInformation = objectiveInformation;
        }

        public String getAssessment() {
            return assessment;
        }

        public void setAssessment(String assessment) {
            this.assessment = assessment;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getIsTest() {
            return isTest;
        }

        public void setIsTest(String isTest) {
            this.isTest = isTest;
        }

        public String getDoctorNotes() {
            return doctorNotes;
        }

        public void setDoctorNotes(String doctorNotes) {
            this.doctorNotes = doctorNotes;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
        @SerializedName("data")
        @Expose
        private Data data;
        @SerializedName("result")
        @Expose
        private String result;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }



}
