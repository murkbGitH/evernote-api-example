package com.evernote.example;

import com.evernote.example.di.JavalibModule;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.user.impl.TestUser;
import com.evernote.example.userstore.UserStoreFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

class BaseEvernoteTest {

    private Injector injector;

    protected UserStoreFactory userStoreFactory;
    protected NoteStoreFactory noteStoreFactory;

    public BaseEvernoteTest() {
        this.userStoreFactory = new UserStoreFactory();
        this.noteStoreFactory = new NoteStoreFactory();

        this.injector = Guice.createInjector(new JavalibModule() {
            @Override
            protected void configure() {
                bind(User.class).to(TestUser.class);
            }
        });
    }

    protected <T> T getInjectedInstance(Class<T> testClass) {
        return injector.getInstance(testClass);
    }
}
