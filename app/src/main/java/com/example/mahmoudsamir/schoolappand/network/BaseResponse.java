package com.example.mahmoudsamir.schoolappand.network;

import android.support.annotation.Nullable;

public class BaseResponse {

    @Nullable
    String errors;
    @Nullable
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
