package com.kajan.iworkflows.view;

import java.io.Serializable;
import java.util.Objects;

public class OauthClient implements Serializable {
    private String name;
    private String redirectUri;
    private String revokeUri;
    private Boolean authorized;

    public OauthClient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRevokeUri() {
        return revokeUri;
    }

    public void setRevokeUri(String revokeUri) {
        this.revokeUri = revokeUri;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OauthClient client = (OauthClient) o;
        return Objects.equals(name, client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
