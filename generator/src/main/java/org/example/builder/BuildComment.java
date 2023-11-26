package org.example.builder;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildComment
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 一个帮助类 BuildComment，用于构建注释
 */

public class BuildComment {
    /**
     * @param bw   BufferedWriter对象
     * @param text 注释文本
     * @return BufferedWriter
     * @description 用于构建类上方的注释
     */
    public static BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" */");
        return bw;
    }

    /**
     * @param bw   BufferedWriter对象
     * @param text 注释文本
     * @return BufferedWriter
     * @description 用于构建属性上方的注释
     */
    public static BufferedWriter buildPropertyComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t */");
        return bw;
    }

    /**
     * @param bw   BufferedWriter对象
     * @param text 注释文本
     * @return BufferedWriter
     * @description 用于构建方法上方的注释
     */
    public static BufferedWriter buildMethodComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t */");
        return bw;
    }
}

