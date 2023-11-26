package org.example.builder;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.StringTools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildMapper
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 一个用于生成Mapper接口文件的工具类
 */

public class BuildMapper {

    public static void execute(TableInfo tableInfo) {
        // 创建一个文件夹对象folder，用于存放生成的Mapper接口文件
        File folder = new File(Constants.PATH_MAPPER);
        // 如果文件夹不存在，则通过folder.mkdirs()创建
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 创建一个File对象beanFile，表示要生成的Mapper接口文件
        File beanFile = new File(Constants.PATH_MAPPER, tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + ".java");
        // 声明三个流对象
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            // 依次初始化三个流对象
            out = new FileOutputStream(beanFile); // 文件输出流
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8); // 字符流
            bw = new BufferedWriter(outw); // 缓冲流
            bw.write("package " + Constants.PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.newLine();
            // 导入语句
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 数据库操作接口");
            bw.newLine();
            // 生成类的注释，并写入接口声明
            bw.write("public interface " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + "<T,P> extends Base"
                    + Constants.SUFFIX_MAPPER + "<T,P> {");

            bw.newLine();

            // 根据表信息中的关键字和关键字段生成Mapper接口中的更新、删除和查询方法
            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            // 遍历keyMap中的每个关键字及其对应的关键字段列表
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                // 获取关键字段列表
                List<FieldInfo> keyColumnList = entry.getValue();
                // 创建两个StringBuffer对象paramStr和methodName，用于存储生成方法的参数列表和方法名
                StringBuffer paramStr = new StringBuffer();
                StringBuffer methodName = new StringBuffer();
                // 初始化一个整数变量index为0，用于追踪当前处理的字段索引
                int index = 0;
                // 遍历关键字段列表keyColumnList，对于每个字段执行以下操作
                for (FieldInfo column : keyColumnList) {
                    // 如果index大于0，表示不是第一个字段
                    if (index > 0) {
                        // 将逗号和"And"追加到paramStr和methodName中，用于拼接参数列表和方法名
                        paramStr.append(",");
                        methodName.append("And");
                    }
                    // 将带有参数名、参数类型和参数属性的字符串追加到paramStr中
                    paramStr.append("@Param(\"" + column.getPropertyName() + "\") " + column.getJavaType() + " "
                            + column.getPropertyName());
                    // 将字段名的首字母大写后追加到methodName中
                    methodName.append(StringTools.upperCaseFirstLetter(column.getPropertyName()));
                    // 将index递增
                    index++;
                }
                // 检查paramStr的长度，如果大于0，表示存在关键字段，则生成更新、删除和查询方法的代码
                if (paramStr.length() > 0) {
                    // 生成方法的注释，并写入更新方法的声明
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "更新");
                    bw.newLine();
                    // 写入更新方法的参数列表和方法体
                    bw.write("\t Integer updateBy" + methodName + "(@Param(\"bean\") T t," + paramStr + ")" + ";");
                    bw.newLine();
                    bw.newLine();
                    // 生成删除方法的代码
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "删除");
                    bw.newLine();
                    bw.write("\t Integer deleteBy" + methodName + "(" + paramStr + ");");
                    bw.newLine();
                    bw.newLine();
                    // 生成查询方法的代码
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "获取对象");
                    bw.newLine();
                    bw.write("\t T selectBy" + methodName + "(" + paramStr + ");");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.flush(); // 刷新缓冲区，确保数据写入文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭打开的文件流
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

