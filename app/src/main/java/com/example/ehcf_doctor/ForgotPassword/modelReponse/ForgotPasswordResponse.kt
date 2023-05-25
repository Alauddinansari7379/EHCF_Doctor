//package com.example.ehcf.PhoneNumber.ModelReponse
//
//
//
//import javax.annotation.Generated;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class ForgotPasswordResponse {
//
//    @SerializedName("result")
//    @Expose
//    private Result result;
//    @SerializedName("message")
//    @Expose
//    private String message;
//    @SerializedName("status")
//    @Expose
//    private Integer status;
//
//    public Result getResult() {
//        return result;
//    }
//
//    public void setResult(Result result) {
//        this.result = result;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//}
//
//public class Result {
//
//    @SerializedName("id")
//    @Expose
//    private Integer id;
//    @SerializedName("otp")
//    @Expose
//    private Integer otp;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getOtp() {
//        return otp;
//    }
//
//    public void setOtp(Integer otp) {
//        this.otp = otp;
//    }
//
//}