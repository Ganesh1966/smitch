package com.example.smitch.models;
import java.util.Objects;

public class LoggedInDetails {
    private User loggedInUser;
    private String jwt;

    public LoggedInDetails(User loggedInUser, String jwt) {
        this.loggedInUser = loggedInUser;
        this.jwt = jwt;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggedInDetails that = (LoggedInDetails) o;
        return Objects.equals(loggedInUser, that.loggedInUser) && Objects.equals(jwt, that.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loggedInUser, jwt);
    }
}