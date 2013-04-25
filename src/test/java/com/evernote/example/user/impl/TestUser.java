package com.evernote.example.user.impl;

import org.apache.commons.lang.StringUtils;

import com.evernote.example.user.User;

/**
 * テスト用のユーザ。
 *
 * @author naotake
 */
public class TestUser implements User {

    @Override
    public String getName() {
        return "TestUser";
    }

    @Override
    public String getDeveloperToken() {
        String developerToken = System.getProperty("developerToken");
        if (StringUtils.isNotEmpty(developerToken)) {
            return developerToken;
        }
        return "*****************************";
    }
}
