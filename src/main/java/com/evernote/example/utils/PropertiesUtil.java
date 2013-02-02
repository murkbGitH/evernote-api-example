package com.evernote.example.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.evernote.example.exception.PropertiesFileLoadException;

/**
 * プロパティファイルに関するユーティリティ。
 *
 * @author naotake
 */
public final class PropertiesUtil {

    /** プロパティファイルの拡張子. */
    private static final String EXTENSION = ".properties";

    /**
     * インスタンス化を抑制。
     */
    private PropertiesUtil() {
        // NOP
    }

    /**
     * 指定されたクラスから解決したパスのプロパティファイルの情報を読み込む。
     *
     * <pre>
     * clazz: com.evernote.example.Foo
     *
     * e.g. com/evernote/example/foo.properties.
     * </pre>
     *
     * @param clazz クラス
     * @return プロパティファイルの情報
     */
    public static Configuration loadConfiguration(Class<?> clazz) {
        Assert.notNull("clazz", clazz);

        String path = clazz.getCanonicalName().replace('.', '/') + EXTENSION;
        return loadConfiguration(path);
    }

    /**
     * 指定されたパスのプロパティファイルの情報を読み込む。
     *
     * @param path プロパティファイルのパス
     * @return プロパティファイルの情報
     */
    public static Configuration loadConfiguration(String path) {
        Assert.notNull("パス", path);

        try {
            return new PropertiesConfiguration(path);
        } catch (ConfigurationException e) {
            throw new PropertiesFileLoadException(e);
        }
    }
}
