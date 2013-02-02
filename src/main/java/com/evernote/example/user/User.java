package com.evernote.example.user;

/**
 * Evernote のユーザ情報を表すインタフェース。
 *
 * @author naotake
 */
public interface User {

    /**
     * ユーザ名を取得する。
     *
     * @return ユーザ名
     */
    String getName();

    /**
     * {@code DeveloperToken}を取得する。
     *
     * @return {@code DeveloperToken}
     */
    String getDeveloperToken();
}
