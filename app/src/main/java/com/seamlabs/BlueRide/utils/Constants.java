package com.seamlabs.BlueRide.utils;

public class Constants {
    public static final String BASE_URL = "http://206.189.211.46/api/";
    public static final String BASE_IMAGES_URL = "http://206.189.211.46/";
    public static final String MAPS_SERVER_KEY = "AIzaSyBVEBhWuQ2lAcRkazdAGxWqmLIDcSUdHdI";
    public static final int REALM_DATABASE_VERSION = 1;

    // Toasts messages
    public static final String SERVER_ERROR = "It's not your fault, It's our";
    public static final String GENERAL_ERROR = "Error occurred, try again later";
    public static final String ADMIN_LOGIN_ERROR = "Admins NOT allowed to log in here";


    // Shared preference for the user
    public static final String SHARED_USER_SETTING = "SHARED_USER_SETTING";
    public static final String SHARED_USER_LOGGING_STATE = "SHARED_USER_LOGGING_STATE";
    public static final String SHARED_USER_TYPE = "SHARED_USER_TYPE";

    // user types
    public static final String PARENT_USER_TYPE = "parent";
    public static final String HELPER_USER_TYPE = "helper";
    public static final String MENTOR_USER_TYPE = "mentor";

    //Students states
    public static final String PENDING_STATE = "pending";
    public static final String PARENT_ARRIVED_STATE = "parent_arrived";
    public static final String REPORTED_STATE = "reported";

    // Extra INTENT data
    public static final String PICK_REQUEST_ID = "pickUpRequestID";
    public static final String SELECTED_SCHOOL_MODEL = "selectedSchoolModel";
    public static final String PARENT_ACTIVITY = "parentActivity";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String USER_NATIONAL_ID = "USER_TYPE";
    public static final String HELPER_ACCOUNT = "HELPER_ACCOUNT";
    public static final String STUDENTS_LIST = "STUDENTS_LIST";
    public static final String PICK_UP_REQUEST_MODEL = "PICK_UP_REQUEST_MODEL";


    // Pick up distance
    public static final double LARGE_DISTANCE = 50.0;
    public static final double SMALL_DISTANCE = 30.0;

    // notification
    public static final String NOTIFICATION_MESSAGE = "You are too close, You can now pick up your children";
    public static final String NOTIFICATION_TITLE = "Pick up your children";
    public static final String CHANEL_ID = "SchoolChanel";
    public static final int NOTIFICATION_ID = 0;
    public static final String PUSHER_API_ID = "554913";
    public static final String PUSHER_API_KEY = "c9b4f87836c5d3ad204a";
    public static final String PUSHER_APP_SECRET = "f924a41bf95e93dace41";
    public static final String PUSHER_API_CLUSTER = "eu";


    //Saudi code
    public static final String PHONE_NUMBER_CODE = "+966";

}
