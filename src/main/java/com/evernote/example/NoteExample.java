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

    /**
     * 指定された名前のノートブックに含まれる全てのノートを検索する。
     *
     * @param notebookName ノートブック名
     * @return ノート一覧
     */
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
     * デフォルトノートブックにノートを作成する。
     *
     * @param title 作成するノートのタイトル
     * @param content 作成するノートの内容
     * @return 作成したノート
     */
    public Note createNoteOnDefaultNotebook(String title, String content) {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // デフォルトノートブックを取得
        Notebook defaultNotebook;
        try {
            defaultNotebook = noteStore.getDefaultNotebook(user
                    .getDeveloperToken());
        } catch (EDAMUserException | EDAMSystemException | TException e) {
            throw new EvernoteException(e);
        }

        // ノート作成
        Note note = createNote(title, content);
        note.setNotebookGuid(defaultNotebook.getGuid());

        try {
            return noteStore.createNote(user.getDeveloperToken(), note);
        } catch (EDAMUserException | EDAMSystemException
                | EDAMNotFoundException | TException e) {
            throw new EvernoteException(e);
        }
    }

    private Note createNote(String title, String content) {

        StringBuilder contentBuffer = new StringBuilder();
        contentBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        contentBuffer.append(//
                "<!DOCTYPE en-note SYSTEM "
                        + "\"http://xml.evernote.com/pub/enml2.dtd\">");
        contentBuffer.append("<en-note>");
        contentBuffer.append(content);
        contentBuffer.append("</en-note>");

        Note note = new Note();
        note.setTitle(title);
        note.setContent(contentBuffer.toString());

        return note;
    }

    /**
     * デフォルトノートブックに含まれる指定したノートを削除する。<br />
     * 同じタイトルのノートが複数存在していた場合、全てのノートを削除する。
     *
     * @param title 削除するノートのタイトル
     */
    public void deleteNoteOnDefaultNotebook(String title) {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        // デフォルトノートブックからノート一覧を取得
        List<Note> notes = findNotesOnDefaultNotebook();

        // 削除
        for (Note note : notes) {
            if (!note.getTitle().equals(title)) {
                continue;
            }

            try {
                noteStore.deleteNote(user.getDeveloperToken(), note.getGuid());
            } catch (EDAMUserException | EDAMSystemException
                    | EDAMNotFoundException | TException e) {
                throw new EvernoteException(e);
            }
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
