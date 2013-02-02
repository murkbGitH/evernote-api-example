package com.evernote.example.user.impl;

import com.evernote.example.user.User;

/**
 * 開発環境のユーザを現す{@link User}の実装クラス。
 *
 * @author naotake
 */
public class Sandbox implements User {

    @Override
    public String getName() {
        return "Sandbox";
    }

    @Override
    public String getDeveloperToken() {
        return "*****************************";
    }
}
