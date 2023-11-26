package org.example.builder;

import org.example.bean.Constants;
import org.example.utils.PropertiesUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildBaseJava
 * @Datetime: 2023/11/01 10:29
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 构建一些基础的 Java 类文件
 */

public class BuildBaseJava {

    public static void execute() {

        // 构建ResponseCodeEnum类
        List<String> headInfoList = new ArrayList();
        headInfoList.add("package " + Constants.PACKAGE_ENUMS);
        build(headInfoList, "ResponseCodeEnum", Constants.PATH_ENUMS);

        // 构建 DateUtils类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_UTILS);
        build(headInfoList, "DateUtil", Constants.PATH_UTILS);

        // 构建日期枚举
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_ENUMS);
        build(headInfoList, "DateTimePatternEnum", Constants.PATH_ENUMS);

        // 构建分页枚举
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_ENUMS);
        build(headInfoList, "PageSize", Constants.PATH_ENUMS);

        // 构建分页类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_PARAM);
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize");
        build(headInfoList, "SimplePage", Constants.PATH_PARAM);

        // 构建分类对象类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_VO);
        headInfoList.add("import java.util.ArrayList");
        headInfoList.add("import java.util.List");
        build(headInfoList, "PaginationResultVO", Constants.PATH_VO);

        // 构建查询类父类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_PARAM);
        build(headInfoList, "BaseParam", Constants.PATH_PARAM);

        // 构建baseMapper
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_MAPPER);
        build(headInfoList, "BaseMapper", Constants.PATH_MAPPER);

        // 构建BusinessException类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_EXCEPTION);
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum");
        build(headInfoList, "BusinessException", Constants.PATH_EXCEPTION);

        // 构建ResponseVO类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_VO);
        build(headInfoList, "ResponseVO", Constants.PATH_VO);

        // 构建ABaseController类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_CONTROLLER);
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum");
        headInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO");
        headInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException");
        build(headInfoList, "ABaseController", Constants.PATH_CONTROLLER);

        // 构建全局错误拦截
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_CONTROLLER);
        headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum");
        headInfoList.add("import " + Constants.PACKAGE_VO + ".ResponseVO");
        headInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException");
        build(headInfoList, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);

        // 构建工具类
        headInfoList.clear();
        headInfoList.add("package " + Constants.PACKAGE_UTILS);
        headInfoList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException");
        headInfoList.add("import java.lang.reflect.Field");
        headInfoList.add("import java.lang.reflect.Method");
        build(headInfoList, "StringTools", Constants.PATH_UTILS);

    }

    /**
     * @param headInfoList
     * @param fileName
     * @param outPutPath
     * @return void
     * @description 根据提供的头部信息列表 (headInfoList)、文件名 (fileName) 和输出路径 (outPutPath)
     *              构建一个 Java 类文件
     */
    private static void build(List<String> headInfoList, String fileName, String outPutPath) {
        // 创建一个 File 对象来表示输出路径
        File folder = new File(outPutPath);
        // 检查该路径是否存在
        if (!folder.exists()) {
            // 如果路径不存在，则使用 mkdirs() 方法创建路径
            folder.mkdirs();
        }
        // 使用提供的文件名和输出路径创建一个新的 File 对象，表示要生成的 Java 类文件
        File beanFile = new File(outPutPath, fileName + ".java");
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader bf = null;
        try {
            // 创建一个 FileOutputStream 对象 out，并将其与新创建的 Java 类文件关联
            out = new FileOutputStream(beanFile);
            // 创建一个 OutputStreamWriter 对象 outw，并将其与 out 对象关联。同时，使用 "utf-8" 编码方式来写入文件。
            outw = new OutputStreamWriter(out, "utf-8");
            // 创建一个 BufferedWriter 对象 bw，并将其与 outw 对象关联，用于提供缓冲写入功能
            bw = new BufferedWriter(outw);
            in = PropertiesUtils.class.getClassLoader().getResourceAsStream("template/" + fileName + ".txt");
            inr = new InputStreamReader(in, "utf-8");
            bf = new BufferedReader(inr);

            for (String str : headInfoList) {
                // 将头部信息逐行写入文件，并在每行后添加分号 (;)
                bw.write(str + ";");
                // 插入空行
                bw.newLine();
            }

            bw.newLine();
            bw.newLine();
            String lineInfo = null;
            // 逐行读取模板文件的内容
            while ((lineInfo = bf.readLine()) != null) {
                // 将每行内容写入新文件
                bw.write(lineInfo);
                bw.newLine();
            }
            // 刷新缓冲区，确保所有内容都被写入文件
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭打开的输入输出流对象，以释放资源并确保文件操作的完成
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inr != null) {
                try {
                    inr.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            if (null != bf) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

