package com.vogtec.utils.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by admin on 2016/8/31.
 * IO流的工具类
 */
public abstract class IOUtils {
    /**
     * 关闭字节流的工具类
     *
     * @param input  输入流
     * @param output 输出流
     */
    public static void close(InputStream input, OutputStream output) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = null;
        }

        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output = null;
        }
    }

    /**
     * 关闭字符流的工具类
     *
     * @param reader
     * @param writer
     */
    public static void close(Reader reader, Writer writer) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader = null;
        }

        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = null;
        }
    }
}
