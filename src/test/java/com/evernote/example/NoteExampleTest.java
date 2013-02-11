package com.evernote.example;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.type.Note;
import com.evernote.example.di.JavalibModule;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.user.impl.TestUser;
import com.evernote.example.userstore.UserStoreFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * ノートに関する動作を検証するテストクラス。
 *
 * @author naotake
 */
public class NoteExampleTest {

    private NoteExample testee;

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
        testee = injector.getInstance(NoteExample.class);

        userStoreFactory = new UserStoreFactory();
        noteStoreFactory = new NoteStoreFactory();

        testee.setUserStoreFactory(userStoreFactory);
        testee.setNoteStoreFactory(noteStoreFactory);
    }

    @Test
    public void 全てのノートを取得できること() {
        List<Note> actuals = testee.findAllNotes();

        assertThat(actuals, hasSize(49));
    }

    @Test
    public void デフォルトノートブックのノート一覧を取得できること() {
        List<Note> actuals = testee.findNotesOnDefaultNotebook();

        assertThat(actuals, hasSize(47));
    }

    @Test
    public void 指定した名前のノートブックのノート一覧を取得できること() {
        List<Note> actuals = testee.findNotes("テスト用ノートブック");

        assertThat(actuals, hasSize(1));
    }

    @Test
    public void 存在しないノートブックの名前を指定した場合_空のListが返されること() {
        List<Note> actuals = testee.findNotes("Hoge");

        assertThat(actuals, empty());
    }

    @Test
    public void 指定したタイトルのノートの作成と削除が行われること() {

        // 事前状態の検証
        List<Note> actuals = testee.findNotesOnDefaultNotebook();
        assertThat(actuals, hasSize(47));

        // ノートの作成
        String title = "20130211ノート";
        Note created = testee.createNoteOnDefaultNotebook(title, "テスト用のノートです。");
        assertThat(created.getGuid(), notNullValue());

        // 作成後の状態を検証
        actuals = testee.findNotesOnDefaultNotebook();
        assertThat(actuals, hasSize(48));

        // ノートの削除
        testee.deleteNoteOnDefaultNotebook(title);

        // 削除後の状態を検証
        actuals = testee.findNotesOnDefaultNotebook();
        assertThat(actuals, hasSize(47));
    }
}
