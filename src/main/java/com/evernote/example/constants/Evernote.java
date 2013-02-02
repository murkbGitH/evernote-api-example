package com.evernote.example.constants;

import org.apache.commons.configuration.Configuration;

import com.evernote.example.utils.PropertiesUtil;

/**
 * Evernote に関する定数クラス。
 *
 * @author naotake
 */
public final class Evernote {

    private static final Configuration CONFIG;
    /**
     * プロパティファイルから設定情報を読み込む。
     */
    static {
        CONFIG = PropertiesUtil.loadConfiguration(Evernote.class);
    }

    /**
     * インスタンス化を抑制。
     */
    private Evernote() {
        // NOP
    }

    /** {@code UserStore}の URL. */
    public static final String USER_STORE_URL = CONFIG
            .getString("userStore.url");

    /** {@code NoteStore}の URL. */
    public static final String NOTE_STORE_URL = CONFIG
            .getString("noteStore.url");
}
