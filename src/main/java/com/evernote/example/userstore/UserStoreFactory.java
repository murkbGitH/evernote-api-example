package com.evernote.example.userstore;

import com.evernote.edam.userstore.UserStore;
import com.evernote.edam.userstore.UserStore.Client;
import com.evernote.example.constants.Evernote;
import com.evernote.example.exception.EvernoteException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;

/**
 * {@link UserStore}を生成するファクトリクラス。
 *
 * @author naotake
 */
public class UserStoreFactory {

    /**
     * {@link UserStore.Client}を生成する。
     *
     * @return {@link UserStore.Client}
     */
    public UserStore.Client create() {

        THttpClient userStoreTrans;
        try {
            userStoreTrans = new THttpClient(Evernote.USER_STORE_URL);
        } catch (TTransportException e) {
            throw new EvernoteException(e);
        }
        TBinaryProtocol userStoreProtocol = new TBinaryProtocol(userStoreTrans);
        UserStore.Client userStoreClient = new Client(userStoreProtocol);

        return userStoreClient;
    }
}
