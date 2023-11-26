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
 * @ClassName: BuildController
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 一个名为 BuildController 的类，用于生成控制器(Controller)的代码
 */

public class BuildController {

    public static void execute(TableInfo tableInfo) {
        // 创建一个文件夹对象folder，用于存放生成的控制器文件
        File folder = new File(Constants.PATH_CONTROLLER);
        // 如果该文件夹不存在，则创建该文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 根据传入的表信息构建控制器文件的路径，并创建文件对象 beanFile，表示要生成的控制器文件
        File beanFile = new File(Constants.PATH_CONTROLLER,
                tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER + ".java");
        // 声明三个流对象
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            // 依次初始化三个流对象
            out = new FileOutputStream(beanFile);
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8); // 指定文件编码为 UTF-8
            bw = new BufferedWriter(outw);
            // 指定控制器所属的包路径
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();
            // 导入所需的类和接口
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PARAM + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + "Service;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");

            // 所有属性
            List<FieldInfo> fieldInfoList = tableInfo.getFieldList();
            bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " Controller");
            bw.newLine();
            // 为控制器类添加注解，指定请求映射路径
            bw.write("@RestController(\"" + StringTools.lowerCaseFirstLetter(tableInfo.getBeanName())
                    + Constants.SUFFIX_CONTROLLER + "\")");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringTools.lowerCaseFirstLetter(tableInfo.getBeanName()) + "\")");
            bw.newLine();
            // 生成控制器类的声明，包括继承自 ABaseController 类的声明
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER
                    + " extends ABaseController{");
            bw.newLine();

            String beanName = tableInfo.getBeanName();

            bw.newLine();
            bw.write("\t@Resource");
            bw.newLine();
            // 生成控制器类的属性声明，其中包括注入的 Service 对象
            String serviceBean = StringTools.lowerCaseFirstLetter(beanName + "Service");
            bw.write("\tprivate " + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " " + serviceBean + ";");

            // 根据条件查询列表
            bw = BuildComment.buildMethodComment(bw, "根据条件分页查询");
            bw.newLine();
            bw.write("\t@RequestMapping(\"/loadDataList\")");
            bw.newLine();
            String paramName = StringTools.lowerCaseFirstLetter(Constants.SUFFIX_BEAN_PARAM);
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParamName() + " " + paramName + "){");
            bw.newLine();

            bw.write("\t\treturn getSuccessResponseVO(" + serviceBean + ".findListByPage(" + paramName + "));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 新增
            bw = BuildComment.buildMethodComment(bw, "新增");
            bw.newLine();
            bw.write("\t@RequestMapping(\"/add\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBean + ".add(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 批量新增的方法
            bw = BuildComment.buildMethodComment(bw, "批量新增");
            bw.newLine();
            bw.write("\t@RequestMapping(\"/addBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBean + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 批量新增的方法
            bw = BuildComment.buildMethodComment(bw, "批量新增/修改");
            bw.newLine();
            bw.write("\t@RequestMapping(\"/addOrUpdateBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName()
                    + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBean + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                List<FieldInfo> keyfieldInfoList = entry.getValue();
                StringBuffer paramStr = new StringBuffer();
                StringBuffer paramNameStr = new StringBuffer();
                StringBuffer methodName = new StringBuffer();
                int index = 0;
                for (FieldInfo column : keyfieldInfoList) {
                    if (index > 0) {
                        paramStr.append(",");
                        paramNameStr.append(",");
                        methodName.append("And");
                    }
                    paramStr.append(column.getJavaType() + " " + column.getPropertyName());
                    paramNameStr.append(column.getPropertyName());
                    methodName.append(StringTools.upperCaseFirstLetter(column.getPropertyName()));
                    index++;
                }
                if (paramStr.length() > 0) {
                    // 根据主键查询
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "查询对象");
                    bw.newLine();
                    String methodNameStr = "get" + tableInfo.getBeanName() + "By" + methodName;
                    bw.write("\t@RequestMapping(\"/" + methodNameStr + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO " + methodNameStr + "(" + paramStr + ") {");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO(" + serviceBean + "." + methodNameStr + "(" + paramNameStr
                            + "));");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    BuildComment.buildMethodComment(bw, "根据" + methodName + "修改对象");
                    bw.newLine();
                    methodNameStr = "update" + tableInfo.getBeanName() + "By" + methodName;
                    bw.write("\t@RequestMapping(\"/" + methodNameStr + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO " + methodNameStr + "(" + tableInfo.getBeanName() + " bean,"
                            + paramStr + ") {");
                    bw.newLine();
                    bw.write("\t\t" + serviceBean + "." + methodNameStr + "(bean," + paramNameStr + ");");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO(null);");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    // 根据主键删除
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "删除");
                    bw.newLine();
                    methodNameStr = "delete" + tableInfo.getBeanName() + "By" + methodName;
                    bw.write("\t@RequestMapping(\"/" + methodNameStr + "\")");
                    bw.newLine();
                    bw.write("\tpublic ResponseVO " + methodNameStr + "(" + paramStr + ") {");
                    bw.newLine();
                    bw.write("\t\t" + serviceBean + "." + methodNameStr + "(" + paramNameStr + ");");
                    bw.newLine();
                    bw.write("\t\treturn getSuccessResponseVO(null);");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                }
            }
            bw.write("}");
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

