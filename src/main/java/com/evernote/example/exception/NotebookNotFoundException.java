package com.evernote.example.exception;

/**
 * 指定した名前のノートブックが存在しなかったことを表す例外クラス。
 *
 * @author naotake
 */
public class NotebookNotFoundException extends Exception {

    private static final long serialVersionUID = -2203636247736764135L;

    private final String notebookName;

    /**
     * コンストラクタ。
     *
     * @param notebookName 存在しなかったノートブック名
     */
    public NotebookNotFoundException(String notebookName) {
        this.notebookName = notebookName;
    }

    /**
     * 存在しなかったノートブック名を取得する。
     *
     * @return ノートブック名
     */
    public String getNotebookName() {
        return notebookName;
    }
}
