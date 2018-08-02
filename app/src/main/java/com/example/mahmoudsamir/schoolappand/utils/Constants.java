package com.example.mahmoudsamir.schoolappand.utils;

public class Constants {
    public static final String BASE_URL = "http://206.189.211.46/api/";
    public static final int REALM_DATABASE_VERSION = 1;

    public static final String SERVER_ERROR = "It's not your fault, But, It's our problem";
    public static final String ERROR = "Error occurred, try again later";


    // Shared preference for the user
    public static final String SHARED_USER_SETTING = "SHARED_USER_SETTING";
    public static final String SHARED_USER_LOGGING_STATE = "SHARED_USER_LOGGING_STATE";
    public static final String SHARED_USER_TYPE = "SHARED_USER_TYPE";

    // user types
    public static final String PARENT_USER_TYPE = "parent";
    public static final String HELPER_USER_TYPE = "helper";
    public static final String MENTOR_USER_TYPE = "mentor";

    // Extra data
    public static final String PICK_REQUEST_ID = "pickUpRequestID";
    public static final String SELECTED_SCHOOL_MODEL = "selectedSchoolModel";
    public static final String PARENT_ACTIVITY = "parentActivity";
    public static final String USER_TYPE = "USER_TYPE";

    // Pick up distance
    public static final double ALLOWED_DISTANCE = 50.0;

    // notification
    public static final String NOTIFICATION_MESSAGE = "You are too close, You can now pick up your children";
    public static final String NOTIFICATION_TITLE = "Pick up your children";
    public static final String CHANEL_ID = "SchoolChanel";
    public static final int NOTIFICATION_ID = 0;
    //    public static final String PUSHER_API_KEY = "226e132ecc8224484a34";
    public static final String PUSHER_API_KEY = "75cfc3e8-7102-40ce-aca1-d715d6384255";
    public static final String PUSHER_API_CLUSTER = "eu";

}
