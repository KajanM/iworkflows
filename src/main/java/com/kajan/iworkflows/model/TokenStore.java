package com.kajan.iworkflows.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class TokenStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String principal;
    private String tokenProvider;
    private String authorizationCode;
    private String accessToken;
    private String refreshToken;

    protected TokenStore() {
    }

    public TokenStore(String principal, String tokenProvider) {
        this.principal = principal;
        this.tokenProvider = tokenProvider;
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

    public String getTokenProvider() {
        return tokenProvider;
    }

    public void setTokenProvider(String tokenProvider) {
        this.tokenProvider = tokenProvider;
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
        TokenStore that = (TokenStore) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TokenStore{" +
                "id=" + id +
                ", principal=" + principal +
                ", tokenProvider=" + tokenProvider +
                ", authorizationCode=" + authorizationCode +
                ", accessToken=" + accessToken +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
