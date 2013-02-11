package com.evernote.example;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.UserStore;
import com.evernote.example.exception.EvernoteException;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.userstore.UserStoreFactory;
import com.evernote.example.utils.Assert;
import com.evernote.thrift.TException;
import com.google.inject.Inject;

/**
 * ノートブックに関するサンプル。
 *
 * @author naotake
 */
public class NotebookExample {

    @Inject
    private User user;

    private UserStoreFactory userStoreFactory;

    private NoteStoreFactory noteStoreFactory;

    /**
     * デフォルトノートブックを取得する。
     *
     * @return デフォルトノートブック
     */
    public Notebook getDefaultNotebook() {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // デフォルトノートブックを取得
        try {
            return noteStore.getDefaultNotebook(user.getDeveloperToken());
        } catch (EDAMUserException | EDAMSystemException | TException e) {
            throw new EvernoteException(e);
        }
    }

    /**
     * 指定された名前のノートブックを取得する。<br />
     * 該当するノートブックが存在しなかった場合、{@code null}を返す。
     *
     * @param notebookName ノートブックの名前
     * @return ノートブック
     */
    public Notebook getNotebook(String notebookName) {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // ノートブックの一覧を取得
        List<Notebook> notebooks;
        try {
            notebooks = noteStore.listNotebooks(user.getDeveloperToken());
        } catch (EDAMUserException | EDAMSystemException | TException e) {
            throw new EvernoteException(e);
        }

        // ノートブック名が一致するものを取得
        Notebook result = null;
        for (Notebook notebook : notebooks) {
            if (StringUtils.equals(notebook.getName(), notebookName)) {
                result = notebook;
                break;
            }
        }
        return result;
    }

    /**
     * 指定された名前のノートブックを作成する。
     *
     * @param notebookName 作成するノートブックの名前（必須）
     * @return 作成したノートブック
     */
    public Notebook createNotebook(String notebookName) {
        Assert.notNull("作成するノートブック名", notebookName);

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // ノートブックを作成
        Notebook notebook = new Notebook();
        notebook.setName(notebookName);

        try {
            return noteStore.createNotebook(user.getDeveloperToken(), notebook);
        } catch (EDAMUserException | EDAMSystemException | TException e) {
            throw new EvernoteException(e);
        }
    }

    /**
     * 指定された名前のノートブックを削除する。
     *
     * @param notebookName 削除するノートブックの名前（必須）
     */
    public void deleteNotebook(String notebookName) {
        Assert.notNull("削除するノートブック名", notebookName);

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // 削除するノートブックを取得
        Notebook notebook = getNotebook(notebookName);

        // 削除
        try {
            noteStore.expungeNotebook(user.getDeveloperToken(),
                    notebook.getGuid());
        } catch (EDAMUserException | EDAMSystemException
                | EDAMNotFoundException | TException e) {
            throw new EvernoteException(e);
        }
    }

    /**
     * {@link UserStoreFactory}を設定する。
     *
     * @param userStoreFactory {@link UserStoreFactory}
     */
    public void setUserStoreFactory(UserStoreFactory userStoreFactory) {
        this.userStoreFactory = userStoreFactory;
    }

    /**
     * {@link NoteStoreFactory}を設定する。
     *
     * @param noteStoreFactory {@link NoteStoreFactory}
     */
    public void setNoteStoreFactory(NoteStoreFactory noteStoreFactory) {
        this.noteStoreFactory = noteStoreFactory;
    }
}
