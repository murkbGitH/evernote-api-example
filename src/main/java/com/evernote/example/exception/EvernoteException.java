package com.evernote.example.exception;

/**
 * Evernote APIで発生する例外をラップした例外クラス。
 *
 * @author naotake
 */
@SuppressWarnings("serial")
public class EvernoteException extends RuntimeException {

    /**
     * 原因となった例外を基にインスタンスを生成する。
     *
     * @param cause 原因
     */
    public EvernoteException(Throwable cause) {
        super(cause);
    }
}
