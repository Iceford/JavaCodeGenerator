package org.example.builder;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildBeanPo
 * @Datetime: 2023/11/01 10:29
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 生成一个 Java Bean 类文件
 */

public class BuildBeanPo {

    public static void execute(TableInfo tableInfo) {
        // 根据指定的常量 Constants.PATH_BEAN 创建一个文件夹对象 folder
        File folder = new File(Constants.PATH_BEAN);
        // 如果该文件夹不存在，则通过 mkdirs() 方法创建该文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 使用提供的 TableInfo 对象和常量 Constants.PATH_BEAN 创建一个 Java Bean 类文件对象 beanFile
        File beanFile = new File(Constants.PATH_BEAN, tableInfo.getBeanName() + ".java");
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            // 初始化一些输出流对象，用于向文件中写入内容
            out = new FileOutputStream(beanFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            /**
             * 通过逐行写入的方式向文件中写入以下内容
             */
            // 指定 Java Bean 类所属的包名
            bw.write("package " + Constants.PACKAGE_BEAN + ";");
            // 空行
            bw.newLine();
            bw.newLine();
            // 根据 Constants.IGNORE_BEAN_TOJSON_CLASS 的值，如果不为空，则导入需要忽略的属性包的类
            if (Constants.IGNORE_BEAN_TOJSON_CLASS != null) {
                bw.write("import " + Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
                bw.newLine();
            }
            // 根据 tableInfo 的信息，如果存在 BigDecimal 类型的字段，则导入 java.math.BigDecimal 类
            if (tableInfo.getHaveBigDecimal() != null && tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            // 根据 tableInfo 的信息，如果存在日期或时间类型的字段，则导入相关的类和枚举
            if (tableInfo.getHaveDate() != null && tableInfo.getHaveDate()
                    || tableInfo.getHaveDateTime() != null && tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_BASE + ".utils.DateUtil;");
                bw.newLine();
                bw.write("import " + Constants.BEAN_DATE_EXPRESSION_CLASS + ";");
                bw.newLine();
                bw.write("import " + Constants.BEAN_DATE_FORMAT_CLASS + ";");
                bw.newLine();
                bw.newLine();

            }
            // 导入 java.io.Serializable 类
            bw.write("import java.io.Serializable;");
            bw.newLine();
            // 调用 BuildComment.buildClassComment() 方法生成类的注释
            bw = BuildComment.buildClassComment(bw, tableInfo.getComment());
            // 空行
            bw.newLine();
            // 写入类的声明，使用 tableInfo 的 getBeanName() 方法获取类名
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            // 空行
            bw.newLine();
            bw.newLine();
            List<FieldInfo> columnList = tableInfo.getFieldList();

            // 通过遍历 tableInfo 的字段列表，逐个生成类的属性
            for (FieldInfo columnInfo : columnList) {
                // 生成属性的注释
                BuildComment.buildPropertyComment(bw, columnInfo.getComment());
                bw.newLine();
                // 如果 Constants.IGNORE_BEAN_TOJSON_COLUMN 不为空，并且当前字段需要被忽略，则写入
                // Constants.IGNORE_BEAN_TOJSON_EXPRESSION 表达式
                if (Constants.IGNORE_BEAN_TOJSON_COLUMN != null) {
                    Boolean ignore = false;
                    for (String column : Constants.IGNORE_BEAN_TOJSON_COLUMN) {
                        if (columnInfo.getFieldName().equalsIgnoreCase(column)) {
                            ignore = true;
                        }
                    }
                    if (ignore) {
                        bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                        bw.newLine();
                    }
                }
                // 如果字段是日期或时间类型，则写入相应的日期表达式和格式化参数
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, columnInfo.getSqlType())
                        || ArrayUtils.contains(Constants.SQL_DATE_TIIME_TYPES,
                                columnInfo.getSqlType())) {
                    String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
                    if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, columnInfo.getSqlType())) {
                        dateTimePattern = "yyyy-MM-dd";
                    }
                    bw.write("\t" + String.format(Constants.BEAN_DATE_EXPRESSION, dateTimePattern));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT, dateTimePattern));
                    bw.newLine();
                }
                // 写入字段的声明，使用 columnInfo 的相关方法获取字段的类型和名称
                bw.write("\tprivate " + columnInfo.getJavaType() + " " + columnInfo.getPropertyName() + ";");
                // 空行
                bw.newLine();
            }
            bw.newLine();

            // 生成每个字段的 getter 和 setter 方法
            String tempField = null;
            for (FieldInfo columnInfo : columnList) {
                tempField = StringTools.upperCaseFirstLetter(columnInfo.getPropertyName());
                bw.newLine();
                // 生成 set 方法，设置字段的值
                bw.write("\tpublic void set" + tempField + "(" + columnInfo.getJavaType() + " "
                        + columnInfo.getPropertyName() + "){");
                bw.newLine();
                bw.write("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                // 生成 get 方法，获取字段的值
                bw.write("\tpublic " + columnInfo.getJavaType() + " get" + tempField + "(){");
                bw.newLine();
                bw.write("\t\treturn this." + columnInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }

            bw.newLine();
            // 重写 toString() 方法，用于生成对象的字符串表示
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString (){");
            StringBuilder tostringStr = new StringBuilder();
            // 它遍历了字段列表，并根据字段的类型生成相应的表达式
            for (FieldInfo columnInfo : columnList) {
                if (Constants.IGNORE_BEAN_TOSTRING_COLUMN != null) {
                    Boolean ignore = false;
                    for (String column : Constants.IGNORE_BEAN_TOSTRING_COLUMN) {
                        if (columnInfo.getFieldName().equalsIgnoreCase(column)) {
                            ignore = true;
                        }
                    }
                    if (ignore) {
                        continue;
                    }
                }
                String properName = columnInfo.getComment();
                properName = StringUtils.isEmpty(properName) ? columnInfo.getPropertyName() : properName;
                String clumn = columnInfo.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TIIME_TYPES, columnInfo.getSqlType())) {
                    clumn = "(" + clumn + " == null ? \"空\" : DateUtil.format(" + clumn + ", " +
                            "DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS" +
                            ".getPattern()))";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, columnInfo.getSqlType())) {
                    clumn = "(" + clumn + " == null ? \"空\" : DateUtil.format(" + clumn + ", " +
                            "DateTimePatternEnum.YYYY_MM_DD" +
                            ".getPattern()))";
                } else {
                    clumn = "(" + clumn + " == null ? \"空\" : " + clumn + ")";
                }

                tostringStr.append("\"，" + properName + ":\"+" + clumn + "+");
            }

            String strResult = "\"" + tostringStr.substring(2, tostringStr.length() - 1) + ";";

            bw.newLine();
            bw.write("\t\treturn " + strResult);
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            bw.write("}");
            bw.newLine();
            // 刷新缓冲区，确保所有内容都被写入文件
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // 关闭打开的输入输出流对象，以释放资源并确保文件操作的完成
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

