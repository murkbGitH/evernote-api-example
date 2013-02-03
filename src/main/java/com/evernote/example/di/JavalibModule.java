package com.evernote.example.di;

import com.evernote.example.user.User;
import com.evernote.example.user.impl.Sandbox;
import com.google.inject.AbstractModule;

/**
 * Guice を使った DI コンテナ。
 *
 * @author naotake
 */
public class JavalibModule extends AbstractModule {

    /**
     * 設定を行う。
     */
    @Override
    protected void configure() {
        bind(User.class).to(Sandbox.class);
    }
}
