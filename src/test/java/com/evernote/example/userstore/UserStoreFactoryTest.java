package com.evernote.example.userstore;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.userstore.UserStore;

/**
 * {@link UserStoreFactory}に対するテストクラス。
 *
 * @author naotake
 */
public class UserStoreFactoryTest {

    private UserStoreFactory testee;

    /**
     * 事前準備。
     */
    @Before
    public void setUp() {
        testee = new UserStoreFactory();
    }

    @Test
    public void UserStoreを取得できること() {
        UserStore.Client actual = testee.create();
        assertThat(actual, notNullValue());
    }
}
