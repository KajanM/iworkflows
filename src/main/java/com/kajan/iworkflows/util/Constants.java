package com.kajan.iworkflows.util;

public class Constants {
    public enum OauthRegistrationId {

        NEXTCLOUD("nextcloud"),
        MOODLE("moodle"),
        LEARN_ORG("learn_org"),
        GOOGLE("google");

        private String registrationId;

        OauthRegistrationId(String registrationId) {
            this.registrationId = registrationId;
        }

        public String getRegistrationId() {
            return registrationId;
        }
    }
}
