package com.example.mahmoudsamir.schoolappand.network;

public class BaseResponse {

    String errors;
    String success;

    public String getErros() {
        return errors;
    }

    public void setErros(String erros) {
        this.errors = erros;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
