package com.kajan.iworkflows.util;

public class Constants {
    public static final String PLACEHOLDER_USERID = "{userid}";
    public static final String PLACEHOLDER_FILE_PATH = "{path-to-file}";
    public static final String PLACEHOLDER_PROVIDER = "{provider}";
    public static final String PLACEHOLDER_MOODLE_WSTOKEN = "{wstoken}";
    public static final String PLACEHOLDER_MOODLE_WSFUNCTION = "{wsfunction}";
    public static final String PLACEHOLDER_LEARNORG_DEPARTMENT = "{role}";
    public static final String PLACEHOLDER_LEARNORG_WSFUNCTION = "{wsfunction}";
    public static final String PLACEHOLDER_LEARNORG_LEAVE_TYPE = "{leave-type}";

    public static final String TOKEN_KEY = "token";
    public static final String ERROR_KEY = "error";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    public static final String LEAVE_ATTACHMENTS_DIR_NAME = "leave-attachments";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String URI_PATH_DELIMITER = "/";

    public enum TokenProvider {

        NEXTCLOUD("nextcloud"),
        MOODLE("moodle"),
        LEARNORG("learnorg"),
        GOOGLE("google");

        private String provider;

        TokenProvider(String provider) {
            this.provider = provider;
        }

        public String getProvider() {
            return provider;
        }
    }
}
