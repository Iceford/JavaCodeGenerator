package org.example.builder;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.StringTools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildBeanQuery
 * @Datetime: 2023/11/01 10:29
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 用于生成参数类文件
 */

public class BuildBeanQuery {

    public static void execute(TableInfo tableInfo) {
        // 创建一个File对象beanFile，表示要生成的参数类文件
        File folder = new File(Constants.PATH_PARAM);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 文件名由表信息中的表名加上常量Constants.SUFFIX_BEAN_PARAM和后缀名.java组成
        File beanFile = new File(Constants.PATH_PARAM, tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + ".java");
        // 声明三个流对象
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            // 依次初始化三个流对象
            out = new FileOutputStream(beanFile);
            // 使用文件输出流out和输出流写入器outw将目标文件与流建立连接，并指定编码为"utf-8"
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            // 使用缓冲写入器bw将输出流写入器包装起来，以提高写入性能
            bw = new BufferedWriter(outw);
            // 写入包名
            bw.write("package " + Constants.PACKAGE_PARAM + ";");
            bw.newLine();
            bw.newLine();

            // 根据表信息中的字段类型判断是否需要导入java.math.BigDecimal和java.util.Date类，并写入对应的导入语句
            if (tableInfo.getHaveBigDecimal() != null && tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            if (tableInfo.getHaveDate() != null && tableInfo.getHaveDate()
                    || tableInfo.getHaveDateTime() != null && tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
            }

            // 生成类注释
            bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + "参数");
            bw.newLine();
            // 写入类的声明，类名由表名加上常量Constants.SUFFIX_BEAN_PARAM组成
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + " extends BaseParam {");
            bw.newLine();
            bw.newLine();
            List<FieldInfo> columnList = tableInfo.getFieldList();

            // 遍历表信息中的字段列表，依次生成属性和相关的getter和setter方法
            for (FieldInfo columnInfo : columnList) {
                BuildComment.buildPropertyComment(bw, columnInfo.getComment());
                bw.newLine();
                // 如果字段类型不是Constants.TYPE_DATE（即非日期类型）
                if (!Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    // 生成对应的属性和getter/setter方法
                    bw.write("\tprivate " + columnInfo.getJavaType() + " " + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                }
                // 生成模糊搜索条件的属性
                if (Constants.TYPE_STRING.equals(columnInfo.getJavaType())) {
                    // 生成对应的属性和getter/setter方法
                    bw.newLine();
                    bw.write("\tprivate " + columnInfo.getJavaType() + " " + columnInfo.getPropertyName()
                            + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                    bw.newLine();
                }
                // 如果是Constants.TYPE_DATE（即日期类型）
                if (Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    // 生成日期属性和对应的起始日期和结束日期的属性以及它们的getter/setter方法
                    bw.write("\tprivate " + Constants.TYPE_STRING + " " + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                    String start = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START;
                    String end = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END;
                    bw.newLine();
                    bw.write("\tprivate " + Constants.TYPE_STRING + " " + start + ";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate " + Constants.TYPE_STRING + " " + end + ";");
                    bw.newLine();
                }
            }
            bw.newLine();

            // 根据表信息中的字段列表生成对应的getter和setter方法
            String tempField = null;
            // 使用一个循环遍历字段列表中的每个字段，对于每个字段，根据字段的类型和名称生成对应的getter和setter方法
            for (FieldInfo columnInfo : columnList) {
                // 根据字段信息设置一个临时变量tempField，它表示字段名称的首字母大写形式，用于生成方法名
                tempField = StringTools.upperCaseFirstLetter(columnInfo.getPropertyName());
                // 根据字段的类型判断是否为日期类型。如果不是日期类型，则生成对应类型的setter和getter方法
                if (!Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    bw.newLine();
                    bw.write("\tpublic void set" + tempField + "(" + columnInfo.getJavaType() + " "
                            + columnInfo.getPropertyName() + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tpublic " + columnInfo.getJavaType() + " get" + tempField + "(){");
                    bw.newLine();
                    bw.write("\t\treturn this." + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                } else {
                    // 如果字段类型是日期类型，则生成特殊的setter和getter方法
                    bw.newLine();
                    bw.write("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " "
                            + columnInfo.getPropertyName() + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    bw.newLine();
                    bw.write("\t\treturn this." + columnInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }

                // 首先检查字段的Java类型是否为字符串类型，如果是则生成一个模糊匹配的setter和getter方法
                if (Constants.TYPE_STRING.equals(columnInfo.getJavaType())) {
                    bw.newLine();
                    bw.write("\tpublic void set" + tempField + Constants.SUFFIX_PROPERTY_FUZZY + "("
                            + columnInfo.getJavaType() + " " + columnInfo.getPropertyName()
                            + Constants.SUFFIX_PROPERTY_FUZZY + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + " = "
                            + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tpublic " + columnInfo.getJavaType() + " get" + tempField
                            + Constants.SUFFIX_PROPERTY_FUZZY
                            + "(){");
                    bw.newLine();
                    bw.write("\t\treturn this." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }

                // 检查字段的Java类型是否为日期类型，如果是，则生成开始日期和结束日期的setter和getter方法
                if (Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    String start = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START;
                    String end = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END;
                    tempField = start.substring(0, 1).toUpperCase() + start.substring(1);
                    // 开始日期
                    bw.newLine();
                    bw.write("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + start + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + start + " = " + start + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    bw.newLine();
                    bw.write("\t\treturn this." + start + ";");
                    bw.newLine();
                    bw.write("\t}");

                    // 结束日期
                    tempField = end.substring(0, 1).toUpperCase() + end.substring(1);
                    bw.newLine();
                    bw.write("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + end + "){");
                    bw.newLine();
                    bw.write("\t\tthis." + end + " = " + end + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    bw.newLine();
                    bw.write("\t\treturn this." + end + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

