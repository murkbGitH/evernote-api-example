package com.evernote.example;

import java.util.List;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.userstore.UserStore;
import com.evernote.example.exception.EvernoteException;
import com.evernote.example.notestore.NoteStoreFactory;
import com.evernote.example.user.User;
import com.evernote.example.userstore.UserStoreFactory;
import com.evernote.thrift.TException;
import com.google.inject.Inject;

/**
 * ノートの検索に関するサンプル。
 *
 * @author naotake
 */
public class NoteFindExample {

    /** ノート検索時の最大数. */
    private static final int MAX_NOTES_SIZE = 1000;

    @Inject
    private User user;

    private UserStoreFactory userStoreFactory;

    private NoteStoreFactory noteStoreFactory;

    public List<Note> findByWord(String word) {

        // UserStore を取得
        UserStore.Client userStore = userStoreFactory.create();

        // NoteStore を取得
        NoteStore.Client noteStore = noteStoreFactory.create(user, userStore);

        NoteFilter filter = new NoteFilter();
        filter.setWords(word);
        filter.setOrder(NoteSortOrder.TITLE.getValue());
        filter.setAscending(true);

        return findNotes(noteStore, filter);
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

    private List<Note> findNotes(NoteStore.Client noteStore, NoteFilter filter) {
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
}
