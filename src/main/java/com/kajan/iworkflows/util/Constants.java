package com.kajan.iworkflows.util;

public class Constants {
    public static final String PLACEHOLDER_USERID = "{userid}";
    public static final String PLACEHOLDER_FILE_PATH = "{path-to-file}";
    public static final String PLACEHOLDER_PROVIDER = "{provider}";
    public static final String PLACEHOLDER_MOODLE_WSTOKEN = "{wstoken}";
    public static final String PLACEHOLDER_MOODLE_WSFUNCTION = "{wsfunction}";
    public static final String PLACEHOLDER_LEARNORG_DEPARTMENT = "{role}";

    public static final String DO_NOTIFY_KEY = "notify";
    public static final String MESSAGE_KEY = "message";
    public static final String STYLE_KEY = "style";
    public static final String STYLE_SUCCESS = "success";
    public static final String STYLE_ERROR = "error";
    public static final String TOKEN_KEY = "token";
    public static final String ERROR_KEY = "error";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String CLIENTS_KEY = "clients";
    public static final String SYSTEM_KEY = "iworkflows";

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
