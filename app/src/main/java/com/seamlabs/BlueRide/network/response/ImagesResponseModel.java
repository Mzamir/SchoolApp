package com.seamlabs.BlueRide.network.response;

import android.support.annotation.Nullable;

import java.io.Serializable;

import static com.seamlabs.BlueRide.utils.Constants.BASE_IMAGES_URL;
import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;

public class ImagesResponseModel implements Serializable {

    int id;
    String path;
    int imageable_id;
    String imageable_type;
    @Nullable
    String created_at;
    @Nullable
    String updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return BASE_IMAGES_URL + path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public int getImageable_id() {
        return imageable_id;
    }

    public void setImageable_id(int imageable_id) {
        this.imageable_id = imageable_id;
    }

    public String getImageable_type() {
        return imageable_type;
    }

    public void setImageable_type(String imageable_type) {
        this.imageable_type = imageable_type;
    }

    @Nullable
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(@Nullable String created_at) {
        this.created_at = created_at;
    }

    @Nullable
    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(@Nullable String updated_at) {
        this.updated_at = updated_at;
    }
}
