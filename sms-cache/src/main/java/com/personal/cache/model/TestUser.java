package com.personal.cache.model;

import java.io.Serializable;

/**
 * @ClassName TestUser
 * @Author liupanpan
 * @Date 2026/1/9
 * @Description
 */
public class TestUser implements Serializable {

    private String username;
    private String password;

    public TestUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public TestUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
