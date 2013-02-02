package com.evernote.example.utils;

/**
 * 事前条件を検証するクラス。
 *
 * @author naotake
 */
public final class Assert {

    /**
     * インスタンス化を抑制。
     */
    private Assert() {
        // NOP
    }

    /**
     * 引数が{@code null}でないことを検証する。
     *
     * @param argName 引数名
     * @param arg 引数
     */
    public static void notNull(String argName, Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " は必須です。");
        }
    }
}
