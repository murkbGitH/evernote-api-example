package com.evernote.example.notestore;

import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.userstore.UserStore;
import com.evernote.example.exception.EvernoteException;
import com.evernote.example.user.User;
import com.evernote.example.utils.Assert;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;

/**
 * {@link NoteStore}を生成するファクトリクラス。
 *
 * @author naotake
 */
public class NoteStoreFactory {

    /**
     * 指定したユーザの{@link UserStore.Client}を生成する。
     *
     * @param user ユーザ情報
     * @param userStore {@link NoteStore.Client}
     * @return {@link UserStore.Client}
     */
    public NoteStore.Client create(User user, UserStore.Client userStore) {
        Assert.notNull("ユーザ情報", user);
        Assert.notNull("UserStore", userStore);

//        String noteStoreUrl;
//        try {
//            noteStoreUrl = userStore.getNoteStoreUrl(user.getDeveloperToken());
//        } catch (EDAMUserException | EDAMSystemException | TException e) {
//            throw new EvernoteException(e);
//        }

        THttpClient noteStoreTrans;
        try {
            noteStoreTrans = new THttpClient("https://sandbox.evernote.com/shard/s1/notestore");
        } catch (TTransportException e) {
            throw new EvernoteException(e);
        }
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);

        return new NoteStore.Client(noteStoreProt);
    }
}
