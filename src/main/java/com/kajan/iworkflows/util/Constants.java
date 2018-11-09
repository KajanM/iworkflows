package com.kajan.iworkflows.util;

public class Constants {
    public static final String PLACEHOLDER_USERID = "{userid}";
    public static final String PLACEHOLDER_FILE_PATH = "{path-to-file}";
    public static final String PLACEHOLDER_PROVIDER = "{provider}";
    public static final String PLACEHOLDER_MOODLE_WSTOKEN = "{wstoken}";
    public static final String PLACEHOLDER_MOODLE_WSFUNCTION = "{wsfunction}";

    public static final String TOKEN_KEY = "token";
    public static final String ERROR_KEY = "error";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

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
