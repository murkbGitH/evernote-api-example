package com.evernote.example.notestore;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.userstore.UserStore;
import com.evernote.example.user.User;
import com.evernote.example.userstore.UserStoreFactory;

/**
 * {@link NoteStoreFactory}に対するテストクラス。
 *
 * @author naotake
 */
public class NoteStoreFactoryTest {

    private NoteStoreFactory testee;
    private UserStoreFactory userStoreFactory;

    /**
     * 事前準備。
     */
    @Before
    public void setUp() {
        testee = new NoteStoreFactory();
        userStoreFactory = new UserStoreFactory();
    }

    @Test
    public void NoteStoreを取得できること() {

        TestUser user = new TestUser();
        UserStore.Client userStore = userStoreFactory.create();

        // 実行
        NoteStore.Client actual = testee.create(user, userStore);

        // 検証
        assertThat(actual, notNullValue());
    }

    /**
     * テスト用のユーザ。
     *
     * @author naotake
     */
    private static final class TestUser implements User {

        @Override
        public String getName() {
            return "TestUser";
        }

        @Override
        public String getDeveloperToken() {
            return "**************";
        }
    }
}
