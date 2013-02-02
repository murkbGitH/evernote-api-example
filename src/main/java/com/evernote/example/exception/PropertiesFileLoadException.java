package com.evernote.example.exception;

/**
 * プロパティファイル読み込み時に例外が発生したことを表す例外クラス。
 *
 * @author naotake
 */
public class PropertiesFileLoadException extends RuntimeException {

    private static final long serialVersionUID = 8939287592227537450L;

    /**
     * 読み込み時の例外を基にインスタンスを生成する。
     *
     * @param cause 読み込み時の例外
     */
    public PropertiesFileLoadException(Throwable cause) {
        super(cause);
    }
}
