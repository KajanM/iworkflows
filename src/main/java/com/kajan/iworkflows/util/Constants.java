package com.kajan.iworkflows.util;

public class Constants {
    public enum OauthProvider {

        NEXTCLOUD("nextcloud"),
        MOODLE("moodle"),
        LEARN_ORG("learn_org"),
        GOOGLE("google");

        private String provider;

        OauthProvider(String provider) {
            this.provider = provider;
        }

        public String getProvider() {
            return provider;
        }
    }
}
