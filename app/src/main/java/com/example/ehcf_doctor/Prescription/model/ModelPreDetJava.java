package com.example.ehcf_doctor.Prescription.model;


 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelPreDetJava {

    @SerializedName("prescriptionid")
    @Expose
    private String prescriptionid;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("diagnosis_name")
    @Expose
    private String diagnosisName;
    @SerializedName("description")
    @Expose
    private String description;

    public String getPrescriptionid() {
        return prescriptionid;
    }

    public void setPrescriptionid(String prescriptionid) {
        this.prescriptionid = prescriptionid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



public class DoctorNote {

    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("doctor_notes")
    @Expose
    private String doctorNotes;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("report")
    @Expose
    private Object report;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("follow_up")
    @Expose
    private Object followUp;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("registration")
    @Expose
    private String registration;
    @SerializedName("clinic_name")
    @Expose
    private String clinicName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Object getReport() {
        return report;
    }

    public void setReport(Object report) {
        this.report = report;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Object getFollowUp() {
        return followUp;
    }

    public void setFollowUp(Object followUp) {
        this.followUp = followUp;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

}


    @SerializedName("doctor_notes")
    @Expose
    private List<DoctorNote> doctorNotes;
    @SerializedName("medicine")
    @Expose
    private List<Medicine> medicine;
    @SerializedName("diagnosis")
    @Expose
    private List<Diagnosi> diagnosis;
    @SerializedName("labtest")
    @Expose
    private List<Labtest> labtest;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<DoctorNote> getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(List<DoctorNote> doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public List<Medicine> getMedicine() {
        return medicine;
    }

    public void setMedicine(List<Medicine> medicine) {
        this.medicine = medicine;
    }

    public List<Diagnosi> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosi> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Labtest> getLabtest() {
        return labtest;
    }

    public void setLabtest(List<Labtest> labtest) {
        this.labtest = labtest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



public class Labtest {

    @SerializedName("prescriptionid")
    @Expose
    private String prescriptionid;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("test_name")
    @Expose
    private Object testName;
    @SerializedName("after")
    @Expose
    private Object after;
    @SerializedName("instructions")
    @Expose
    private Object instructions;

    public String getPrescriptionid() {
        return prescriptionid;
    }

    public void setPrescriptionid(String prescriptionid) {
        this.prescriptionid = prescriptionid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getTestName() {
        return testName;
    }

    public void setTestName(Object testName) {
        this.testName = testName;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }

    public Object getInstructions() {
        return instructions;
    }

    public void setInstructions(Object instructions) {
        this.instructions = instructions;
    }

}

public class Medicine {

    @SerializedName("prescriptionid")
    @Expose
    private String prescriptionid;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("intake")
    @Expose
    private String intake;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("duration")
    @Expose
    private String duration;

    public String getPrescriptionid() {
        return prescriptionid;
    }

    public void setPrescriptionid(String prescriptionid) {
        this.prescriptionid = prescriptionid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
}