package com.evernote.example;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.type.Notebook;
import com.evernote.example.di.JavalibModule;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.user.impl.TestUser;
import com.evernote.example.userstore.UserStoreFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * ノートブックに関する動作を検証するテストクラス。
 *
 * @author naotake
 */
public class NotebookExampleTest {

    private NotebookExample testee;

    private UserStoreFactory userStoreFactory;
    private NoteStoreFactory noteStoreFactory;

    /**
     * 事前準備。
     */
    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new JavalibModule() {
            @Override
            protected void configure() {
                bind(User.class).to(TestUser.class);
            }
        });
        testee = injector.getInstance(NotebookExample.class);

        userStoreFactory = new UserStoreFactory();
        noteStoreFactory = new NoteStoreFactory();

        testee.setUserStoreFactory(userStoreFactory);
        testee.setNoteStoreFactory(noteStoreFactory);
    }

    @Test
    public void デフォルトノートブックを取得できること() {
        Notebook actual = testee.getDefaultNotebook();

        assertThat(actual, notNullValue());
    }

    @Test
    public void 指定した名前のノートブックを取得できること() {
        String notebookName = "テスト用ノートブック";

        Notebook actual = testee.getNotebook(notebookName);

        assertThat(actual, notNullValue());
        assertThat(actual.getName(), is(notebookName));
    }

    @Test
    public void 指定した名前のノートブックが存在しなかった場合はnullが返されること() {
        Notebook actual = testee.getNotebook("hoge");

        assertThat(actual, nullValue());
    }

    @Test
    public void ノートブックの作成と削除が行われること() {
        String notebookName = "DummyNotebook";

        // 事前状態の検証
        Notebook actual = testee.getNotebook(notebookName);
        assertThat(actual, nullValue());

        // 作成
        Notebook registered = testee.createNotebook(notebookName);

        // 検証
        actual = testee.getNotebook(notebookName);
        assertThat(actual, notNullValue());
        assertThat(actual.getGuid(), is(registered.getGuid()));

        // 削除
        testee.deleteNotebook(notebookName);

        // 事後状態の検証
        actual = testee.getNotebook(notebookName);
        assertThat(actual, nullValue());
    }
}
