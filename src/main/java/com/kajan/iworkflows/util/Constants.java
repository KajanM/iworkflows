package com.kajan.iworkflows.util;

public class Constants {
    public enum TokenProvider {

        NEXTCLOUD("nextcloud"),
        MOODLE("moodle"),
        LEARN_ORG("learn_org"),
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
