package com.kajan.iworkflows.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Oauth2TokenStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String principal;
    private String oauthProvider;
    private String authorizationCode;
    private String accessToken;
    private String refreshToken;

    protected Oauth2TokenStore() {
    }

    public Oauth2TokenStore(String principal, String oauthProvider) {
        this.principal = principal;
        this.oauthProvider = oauthProvider;
    }

    public Long getId() {
        return id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oauth2TokenStore that = (Oauth2TokenStore) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Oauth2TokenStore{" +
                "id=" + id +
                ", principal=" + principal +
                ", oauthProvider=" + oauthProvider +
                ", authorizationCode=" + authorizationCode +
                ", accessToken=" + accessToken +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
