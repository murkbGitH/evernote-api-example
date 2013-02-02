package com.evernote.example.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

/**
 * {@link PropertiesUtil}に対するテストクラス。
 *
 * @author naotake
 */
public class PropertiesUtilTest {

    @Test
    public void 指定したクラスと同名のプロパティファイルを読み込めること() {
        Configuration actual = PropertiesUtil
                .loadConfiguration(PropertiesUtilTest.class);

        assertThat(actual.getString("message"), is("クラスを指定"));
        assertThat(actual.getInt("value"), is(1230));
    }

    @Test
    public void 指定したパスのプロパティファイルを読み込めること() {
        String path = "com/evernote/example/utils/test.properties";
        Configuration actual = PropertiesUtil.loadConfiguration(path);

        assertThat(actual.getString("message"), is("パスを指定"));
        assertThat(actual.getInt("value"), is(5410));
    }
}
