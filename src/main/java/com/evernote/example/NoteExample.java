package com.evernote.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.UserStore;
import com.evernote.example.exception.EvernoteException;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.userstore.UserStoreFactory;
import com.evernote.thrift.TException;
import com.google.inject.Inject;

/**
 * ノートに関するサンプル。
 *
 * @author naotake
 */
public class NoteExample {

    /** ノート検索時の最大数. */
    private static final int MAX_NOTES_SIZE = 1000;

    @Inject
    private User user;

    private UserStoreFactory userStoreFactory;

    private NoteStoreFactory noteStoreFactory;

    /**
     * ユーザが作成した全てのノートを検索する。
     *
     * @return ノート一覧
     */
    public List<Note> findAllNotes() {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // ノート一覧を取得する
        NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);

        NoteFilter filter = new NoteFilter();
        filter.setOrder(NoteSortOrder.CREATED.getValue());

        NoteList notes;
        try {
            notes = noteStore.findNotes(//
                    user.getDeveloperToken(), filter, 0, MAX_NOTES_SIZE);
        } catch (EDAMUserException | EDAMSystemException
                | EDAMNotFoundException | TException e) {
            throw new EvernoteException(e);
        }

        return notes.getNotes();
    }

    /**
     * デフォルトノートブックに含まれる全てのノートを検索する。
     *
     * @return ノート一覧
     */
    public List<Note> findNotesOnDefaultNotebook() {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // デフォルトノートブックを取得
        Notebook notebook;
        try {
            notebook = noteStore.getDefaultNotebook(user.getDeveloperToken());
        } catch (EDAMUserException | EDAMSystemException | TException e) {
            throw new EvernoteException(e);
        }

        // ノート一覧を取得する
        NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);

        NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid(notebook.getGuid());
        filter.setOrder(NoteSortOrder.CREATED.getValue());

        NoteList notes;
        try {
            notes = noteStore.findNotes(//
                    user.getDeveloperToken(), filter, 0, MAX_NOTES_SIZE);
        } catch (EDAMUserException | EDAMSystemException
                | EDAMNotFoundException | TException e) {
            throw new EvernoteException(e);
        }

        return notes.getNotes();
    }

    public List<Note> findNotes(String notebookName) {

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
        if (result == null) {
            return new ArrayList<>(0);
        }

        // ノート一覧を取得する
        NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
        spec.setIncludeTitle(true);

        NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid(result.getGuid());
        filter.setOrder(NoteSortOrder.CREATED.getValue());

        NoteList notes;
        try {
            notes = noteStore.findNotes(//
                    user.getDeveloperToken(), filter, 0, MAX_NOTES_SIZE);
        } catch (EDAMUserException | EDAMSystemException
                | EDAMNotFoundException | TException e) {
            throw new EvernoteException(e);
        }

        return notes.getNotes();
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
