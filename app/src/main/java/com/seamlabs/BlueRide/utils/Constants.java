package com.seamlabs.BlueRide.utils;

public class Constants {
    public static final String BASE_URL = "https://blueride.app:555/api/";
    public static final String FORGET_PASSWORD_URL = "https://blueride.app:555/password/reset";
    public static final String BASE_IMAGES_URL = "https://blueride.app:555";
    public static final String MAPS_SERVER_KEY = "AIzaSyBVEBhWuQ2lAcRkazdAGxWqmLIDcSUdHdI";
    public static final int REALM_DATABASE_VERSION = 1;
    public static final String TERMS_AND_CONDITIONS_FOLDER_NAME = "BlueRide";
    public static final String TERMS_AND_CONDITIONS_FILE_NAME = "TermsAndConditions.pdf";
    // Toasts messages
    public static final String SERVER_ERROR = "Error occurred";
    public static final String GENERAL_ERROR = "Error occurred, try again later";
    public static final String ADMIN_LOGIN_ERROR = "You'r NOT allowed to logIn here";
    public static CharSequence EMPTY_FIELD_ERROR = "Empty field";

    // Shared preference for the user
    public static final String SHARED_USER_SETTING = "SHARED_USER_SETTING";
    public static final String SHARED_USER_LOGGING_STATE = "SHARED_USER_LOGGING_STATE";
    public static final String SHARED_USER_TYPE = "SHARED_USER_TYPE";
    public static final String SHARED_USER_LANGUAGE= "SHARED_USER_LANGUAGE";

    // Shared preference for pending students
    public static final String SHARED_PENDING_STUDENTS = "SHARED_PENDING_STUDENTS";
    public static final String SHARED_PENDING_STUDENTS_LIST = "SHARED_PENDING_STUDENTS_LIST";

    // user types
    public static final String PARENT_USER_TYPE = "parent";
    public static final String HELPER_USER_TYPE = "helper";
    public static final String MENTOR_USER_TYPE = "mentor";
    public static final String TEACHER_USER_TYPE = "teacher";

    //Students states
    public static final String PENDING_STATE = "Get Ready";
    public static final String PARENT_ARRIVED_STATE = "Let’s Go";
    public static final String REPORTED_STATE = "Hurry Up!";
    public static final String DELIVERD_TO_SUPERVISON = "With Supervisor";

    // Extra INTENT data
    public static final String PICK_REQUEST_ID = "pickUpRequestID";
    public static final String SELECTED_SCHOOL_MODEL = "selectedSchoolModel";
    public static final String PARENT_ACTIVITY = "parentActivity";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String USER_NATIONAL_ID = "USER_NATIONAL_ID";
    public static final String USER_ID = "USER_ID";
    public static final String HELPER_ACCOUNT = "HELPER_ACCOUNT";
    public static final String STUDENTS_LIST = "STUDENTS_LIST";
    public static final String PICK_UP_REQUEST_MODEL = "PICK_UP_REQUEST_MODEL";
    public static final String HELPER_LATITUDE = "HELPER_LATITUDE";
    public static final String HELPER_LONGITUDE = "HELPER_LONGITUDE";
    public static final String TRACKED_HELPER_ID = "TRACKED_HELPER_ID";
    public static final String FRAGMENT_TO_SHOW = "FragmentToShow";
    public static final String URL_TO_LOAD_IN_WEBVIEW = "URL_TO_LOAD_IN_WEBVIEW";

    // side menu fragments names
    public static final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";
    public static final String EDIT_PROFILE_FRAGMENT = "EDIT_PROFILE_FRAGMENT";
    public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final String TRACKING_FRAGMENT = "TRACKING_FRAGMENT";

    // Pick up distance
    public static double LARGE_DISTANCE;
    public static double SMALL_DISTANCE;

    // notification
    // local notification
    public static final String NOTIFICATION_MESSAGE = "You are too close, You can now pick up your children";
    public static final String NOTIFICATION_TITLE = "Pick up your children";
    public static final String CHANEL_ID = "SchoolChanel";
    public static final int NOTIFICATION_ID = 0;

    // pusher notification
    public static final String PUSHER_API_ID = "554913";
    public static final String PUSHER_BEAMS_INSTANCE_ID= "602b2180-d476-4c1f-8ca2-fa42230aaaef";
    public static final String PUSHER_APP_SECRET = "7CDA2D489F2DAFBC1DF03184EFC5A45";
    public static final String PUSHER_API_KEY = "c9b4f87836c5d3ad204a";
    public static final String PUSHER_API_CLUSTER = "eu";


    public static final String PUSHER_TRACKING_CHANEL_NAME = "updateCoordinate";
    public static final String PUSHER_TRACKING_EVENT_NAME = "App\\Events\\TrackUser";

    public static final String PUSHER_MENTOR_CHANEL_NAME = "mentorQueue";
    public static final String PUSHER_MENTOR_EVENT_NAME = "App\\Events\\MentorRequest";

    public static final String PUSHER_SECURITY_CHANEL_NAME = "activateReceived";
    public static final String PUSHER_SECURITY_EVENT_NAME = "App\\Events\\SecurityDeliver";

    //Saudi code
    public static final String PHONE_NUMBER_CODE = "+966";

    // Picking-up period and permission
    public static final int NOT_ALLOWED_TO_PICK_UP = 0;
    public static final int ONE_TIME_PICK_UP = 1;
    public static final int ALWAYS_PICK_UP = 2;

    // Events
    public static final String EVENT_PICTURE_CHANGED = "ProfilePictureChanged";

    // Languages
    public static final String ARABIC = "ar";
    public static final String ENGLISH = "en";

}
