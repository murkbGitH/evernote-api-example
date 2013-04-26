package com.evernote.example;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.type.Note;
import com.evernote.example.NoteExampleTest.個別のノートブックにノートを作成する場合;

/**
 * 検索構文を使用したノート検索の動作を検証するテストクラス。
 *
 * @author naotake
 */
public class NoteFindExampleTest extends BaseEvernoteTest {

    private NoteFindExample testee;
    private List<Note> actuals;

    /**
     * 事前準備。
     */
    @Before
    public void setUp() {
        testee = super.getInjectedInstance(NoteFindExample.class);

        testee.setUserStoreFactory(userStoreFactory);
        testee.setNoteStoreFactory(noteStoreFactory);

        actuals = null;
    }

    @Test
    public void 指定した名前のノートブックのノートを取得できること() {
        actuals = testee.findByWord("notebook:テスト用ノートブック");
        assertThat(actuals, hasSize(1));
    }

    @Test
    public void 指定したタグ名のノートを取得できること() {
        actuals = testee.findByWord("tag:cookpad");
        assertThat(actuals, hasSize(1));
        assertThat(actuals.get(0).getTitle(), is("個別ノート2-1"));

        actuals = testee.findByWord("any: tag:cooking cookpad");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート1-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート2-1"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート2-2"));
    }

    @Test
    public void 先頭一致したタグを含んだノートを取得できること() {
        actuals = testee.findByWord("tag:cook*");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート1-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート2-1"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート2-2"));
    }

    @Test
    public void 指定したタグを含まないノートを取得できること() {
        actuals = testee.findByWord("notebook:個別ブック2");
        assertThat(actuals, hasSize(2));
        assertThat(actuals.get(0).getTitle(), is("個別ノート2-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート2-2"));

        actuals = testee.findByWord("notebook:個別ブック2 -tag:cooking");
        assertThat(actuals, hasSize(1));
        assertThat(actuals.get(0).getTitle(), is("個別ノート2-1"));
    }

    @Test
    public void タグを含まないノートを取得できること() {
        actuals = testee.findByWord("notebook:個別ブック3");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート3-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート3-2"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート3-3"));

        actuals = testee.findByWord("notebook:個別ブック3 -tag:*");
        assertThat(actuals, hasSize(2));
        assertThat(actuals.get(0).getTitle(), is("個別ノート3-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート3-3"));
    }

    @Test
    public void 指定したタイトルを含むノートを取得できること() {
        actuals = testee.findByWord("intitle:個別ノート3");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート3-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート3-2"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート3-3"));
    }

    @Test
    public void 指定したタイトルを含まないノートを取得できること() {
        actuals = testee.findByWord("notebook:個別ブック2 -intitle:個別ノート2-1");
        assertThat(actuals, hasSize(1));
        assertThat(actuals.get(0).getTitle(), is("個別ノート2-2"));
    }

    @Test
    public void 本日作成されたノートを取得できること() throws Exception {
        actuals = testee.findByWord("notebook:テスト用ノートブック created:day");
        assertThat(actuals, empty());

        // ノートを作成する
        個別のノートブックにノートを作成する場合 ノートを作成する = new 個別のノートブックにノートを作成する場合();
        ノートを作成する.setUp();
        ノートを作成する.指定したタイトルのノートの作成と削除が行われること();

        // 検証
        actuals = testee.findByWord("notebook:テスト用ノートブック created:day");
        assertThat(actuals, hasSize(1));

        ノートを作成する.tearDown();
    }

    @Test
    public void _2013年04月24日に作成されたノートを取得できること() {
        actuals = testee.findByWord("created:20130424 -created:20130425");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート1-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート2-1"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート2-2"));
    }

    /**
     * 本当は作成日が 2013/4/27 の深夜だが、タイムゾーンの関係で 27 日指定だと正しく取得できない。<br />
     * タイムゾーンが US 基準になってる。
     */
    @Test
    public void _2013年04月27日に作成されたノートを取得できること() {
        actuals = testee.findByWord("created:20130426 -created:20130427");
        assertThat(actuals, hasSize(3));
        assertThat(actuals.get(0).getTitle(), is("個別ノート3-1"));
        assertThat(actuals.get(1).getTitle(), is("個別ノート3-2"));
        assertThat(actuals.get(2).getTitle(), is("個別ノート3-3"));
    }
}
