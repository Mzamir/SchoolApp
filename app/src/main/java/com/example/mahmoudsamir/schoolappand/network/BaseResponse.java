package com.example.mahmoudsamir.schoolappand.network;

import android.support.annotation.Nullable;

public class BaseResponse {

    @Nullable
    String errors;
    @Nullable
    String success;
    @Nullable
    String message;

    @Nullable
    public String getErrors() {
        return errors;
    }

    public void setErrors(@Nullable String errors) {
        this.errors = errors;
    }

    @Nullable
    public String getSuccess() {
        return success;
    }

    public void setSuccess(@Nullable String success) {
        this.success = success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
