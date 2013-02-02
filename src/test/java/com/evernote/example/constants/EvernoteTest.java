package com.evernote.example.constants;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link Evernote}に対するテストクラス。
 *
 * @author naotake
 */
public class EvernoteTest {

    @Test
    public void Evernoteに定義されている定数を参照できること() {
        assertThat(Evernote.USER_STORE_URL,
                is("https://sandbox.evernote.com/edam/user"));
        assertThat(Evernote.NOTE_STORE_URL,
                is("https://sandbox.evernote.com/edam/note"));
    }
}
