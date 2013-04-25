package com.evernote.example;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.type.Note;
import com.evernote.example.di.JavalibModule;
import com.evernote.example.exception.NotebookNotFoundException;
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
@RunWith(Enclosed.class)
public class NoteExampleTest {

    public static class ノートの検索を行う場合 {

        private NoteExample testee;

        private UserStoreFactory userStoreFactory;
        private NoteStoreFactory noteStoreFactory;

        /**
         * 事前準備。
         */
        @Before
        public void setUp() {
            testee = getInjectedInstance();

            userStoreFactory = new UserStoreFactory();
            noteStoreFactory = new NoteStoreFactory();

            testee.setUserStoreFactory(userStoreFactory);
            testee.setNoteStoreFactory(noteStoreFactory);
        }

        @Test
        public void 全てのノートを取得できること() {
            List<Note> actuals = testee.findAllNotes();

            assertThat(actuals, hasSize(50));
        }

        @Test
        public void 全てのノートメタデータを取得できること() {
            List<NoteMetadata> actuals = testee.findAllNoteMetadatas();

            assertThat(actuals, hasSize(55));
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
    }

    public static class デフォルトノートブックにノートの作成を行う場合 {

        private NoteExample testee;

        private UserStoreFactory userStoreFactory;
        private NoteStoreFactory noteStoreFactory;
        private String targetTitle;

        /**
         * 事前準備。
         */
        @Before
        public void setUp() {
            testee = getInjectedInstance();

            userStoreFactory = new UserStoreFactory();
            noteStoreFactory = new NoteStoreFactory();

            testee.setUserStoreFactory(userStoreFactory);
            testee.setNoteStoreFactory(noteStoreFactory);

            // 事前状態の検証
            List<Note> actuals = testee.findNotesOnDefaultNotebook();
            assertThat(actuals, hasSize(47));
        }

        /**
         * 事後処理。
         */
        @After
        public void tearDown() {
            // ノートの削除
            testee.deleteNoteOnDefaultNotebook(targetTitle);

            // 削除後の状態を検証
            List<Note> actuals = testee.findNotesOnDefaultNotebook();
            assertThat(actuals, hasSize(47));
        }

        @Test
        public void 指定したタイトルのノートの作成と削除が行われること() {

            // ノートの作成
            targetTitle = "20130211ノート";
            Note created = testee.createNoteOnDefaultNotebook(targetTitle,
                    "テスト用のノートです。");
            assertThat(created.getGuid(), notNullValue());

            // 作成後の状態を検証
            List<Note> actuals = testee.findNotesOnDefaultNotebook();
            assertThat(actuals, hasSize(48));
        }

        @Test
        public void 指定したタグを付けたノートの作成と削除が行われること() {

            // ノートの作成
            targetTitle = "20130217ノート";
            Note created = testee.createNoteOnDefaultNotebook(targetTitle,
                    "テスト用のノートです。", "Foo", "Bar");
            assertThat(created.getGuid(), notNullValue());

            // 作成後の状態を検証
            List<Note> actuals = testee.findNotesOnDefaultNotebook();
            assertThat(actuals, hasSize(48));
        }

    }

    public static class 個別のノートブックにノートを作成する場合 {

        private NoteExample testee;

        private UserStoreFactory userStoreFactory;
        private NoteStoreFactory noteStoreFactory;
        private String targetNotebookName;
        private String targetTitle;

        /**
         * 事前準備。
         */
        @Before
        public void setUp() {
            testee = getInjectedInstance();

            userStoreFactory = new UserStoreFactory();
            noteStoreFactory = new NoteStoreFactory();

            testee.setUserStoreFactory(userStoreFactory);
            testee.setNoteStoreFactory(noteStoreFactory);

            // 事前状態の検証
            targetNotebookName = "テスト用ノートブック";
            List<Note> actuals = testee.findNotes(targetNotebookName);
            assertThat(actuals, hasSize(1));
        }

        /**
         * 事後処理。
         */
        @After
        public void tearDown() {
            // ノートの削除
            testee.deleteNote(targetTitle, targetNotebookName);

            // 削除後の状態を検証
            List<Note> actuals = testee.findNotes(targetNotebookName);
            assertThat(actuals, hasSize(1));
        }

        @Test
        public void 指定したタイトルのノートの作成と削除が行われること()
                throws NotebookNotFoundException {

            // ノートの作成
            targetTitle = "20130211ノート";
            Note created = testee.createNote(targetTitle, "テスト用のノートです。",
                    targetNotebookName);
            assertThat(created.getGuid(), notNullValue());

            // 作成後の状態を検証
            List<Note> actuals = testee.findNotes(targetNotebookName);
            assertThat(actuals, hasSize(2));

        }
    }

    static NoteExample getInjectedInstance() {
        Injector injector = Guice.createInjector(new JavalibModule() {
            @Override
            protected void configure() {
                bind(User.class).to(TestUser.class);
            }
        });
        return injector.getInstance(NoteExample.class);
    }
}
