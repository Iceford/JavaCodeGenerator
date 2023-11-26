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
 * @ClassName: BuildServiceImpl
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 一个于构建服务实现类的工具类，用于根据传入的TableInfo对象生成一个服务实现类的Java文件
 */

public class BuildServiceImpl {

    public static void execute(TableInfo tableInfo) {
        // 创建了一个 File 对象 folder，用于表示指定路径
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        // 如果该文件夹不存在，就调用 mkdirs() 方法创建它
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 根据传入的 tableInfo 对象获取了一个文件名 beanFile
        File beanFile = new File(Constants.PATH_SERVICE_IMPL,
                tableInfo.getBeanName() + Constants.SUFFIX_SERVICE_IMPL + ".java");
        // 声明三个流对象
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        try {
            // 依次初始化三个流对象
            out = new FileOutputStream(beanFile);
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);
            // 将包名写入文件中
            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PARAM + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PARAM + ".SimplePage;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPER + "." + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER
                    + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE
                    + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_UTILS + ".StringTools;");
            bw.newLine();

            // 调用 BuildComment.buildClassComment 方法向文件中添加类级别的注释
            bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 业务接口实现");
            bw.newLine();
            // 生成一个映射器类的名称
            String anServiceBean = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
            // 将映射器类的名称的首字母转换为小写
            anServiceBean = anServiceBean.substring(0, 1).toLowerCase() + anServiceBean.substring(1);
            bw.write("@Service(\"" + anServiceBean + "\")");
            bw.newLine();
            bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE_IMPL + " implements "
                    + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " {");
            bw.newLine();
            bw.newLine();
            bw.write("\t@Resource");
            bw.newLine();

            String beanName = tableInfo.getBeanName();
            String paramMapper = beanName + Constants.SUFFIX_MAPPER;
            paramMapper = paramMapper.substring(0, 1).toLowerCase() + paramMapper.substring(1);
            bw.write("\tprivate " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + "<" + tableInfo.getBeanName()
                    + ", " + tableInfo.getBeanParamName() + "> " + paramMapper + ";");
            bw.newLine();

            // 根据条件查询列表
            bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + beanName + "> findListByParam(" + tableInfo.getBeanParamName() + " param) {");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".selectList(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 根据条件查询记录数
            bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " param) {");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".selectCount(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 分页查询的方法
            bw = BuildComment.buildMethodComment(bw, "分页查询方法");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic PaginationResultVO<" + beanName + "> findListByPage(" + tableInfo.getBeanParamName()
                    + " param) {");
            bw.newLine();
            bw.write("\t\tint count = this.findCountByParam(param);");
            bw.newLine();
            bw.write(
                    "\t\tint pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();");
            bw.newLine();
            bw.newLine();
            bw.write("\t\tSimplePage page = new SimplePage(param.getPageNo(), count, pageSize);");
            bw.newLine();
            bw.write("\t\tparam.setSimplePage(page);");
            bw.newLine();
            bw.write("\t\tList<" + beanName + "> list = this.findListByParam(param);");
            bw.newLine();
            bw.write("\t\tPaginationResultVO<" + beanName
                    + "> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);");
            bw.newLine();
            bw.write("\t\treturn result;");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 新增
            bw = BuildComment.buildMethodComment(bw, "新增");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 批量新增
            bw = BuildComment.buildMethodComment(bw, "批量新增");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".insertBatch(listBean);");

            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 批量新增
            bw = BuildComment.buildMethodComment(bw, "批量新增或者修改");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".insertOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 多条件更新
            bw = BuildComment.buildMethodComment(bw, "多条件更新");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer updateByParam(" + tableInfo.getBeanName() + " bean, "
                    + tableInfo.getBeanParamName() + " param) {");
            bw.newLine();
            bw.write("\t\tStringTools.checkParam(param);");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".updateByParam(bean, param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            // 多条件删除
            bw = BuildComment.buildMethodComment(bw, "多条件删除");
            bw.newLine();
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer deleteByParam(" + tableInfo.getBeanParamName() + " param) {");
            bw.newLine();
            bw.write("\t\tStringTools.checkParam(param);");
            bw.newLine();
            bw.write("\t\treturn this." + paramMapper + ".deleteByParam(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            // 遍历 keyMap 的键值对，逐个处理每个主键字段
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                // 获取主键字段列表
                List<FieldInfo> keyColumnList = entry.getValue();
                // 用于构建方法的参数列表
                StringBuffer paramStr = new StringBuffer();
                // 用于构建方法的数值列表
                StringBuffer paramValueStr = new StringBuffer();
                // 用于构建方法的方法名
                StringBuffer methodName = new StringBuffer();
                int index = 0;
                // 使用一个循环遍历 keyColumnList 中的字段，同时维护一个索引变量 index
                for (FieldInfo column : keyColumnList) {
                    if (index > 0) {
                        paramStr.append(", ");
                        methodName.append("And");
                        paramValueStr.append(", ");
                    }
                    paramStr.append(column.getJavaType() + " " + column.getPropertyName());
                    paramValueStr.append(column.getPropertyName());
                    methodName.append(StringTools.upperCaseFirstLetter(column.getPropertyName()));
                    index++;
                }
                // 通过检查 paramStr 的长度是否大于0，判断是否需要生成方法代码
                if (paramStr.length() > 0) {
                    // 根据主键查询
                    BuildComment.buildMethodComment(bw, "根据" + methodName + "获取对象");
                    bw.newLine();
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By"
                            + methodName + "(" + paramStr + ") {");
                    bw.newLine();
                    bw.write("\t\treturn this." + paramMapper + ".selectBy" + methodName + "(" + paramValueStr + ");");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    // 根据主键修改
                    bw = BuildComment.buildMethodComment(bw, "根据" + methodName + "修改");
                    bw.newLine();
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "("
                            + tableInfo.getBeanName() + " bean, " + paramStr + ") {");
                    bw.newLine();
                    bw.write("\t\treturn this." + paramMapper + ".updateBy" + methodName + "(bean, " + paramValueStr
                            + ");");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    // 根据主键删除
                    bw = BuildComment.buildMethodComment(bw, "根据" + methodName + "删除");
                    bw.newLine();
                    bw.write("\t@Override");
                    bw.newLine();
                    bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + paramStr
                            + ") {");
                    bw.newLine();
                    bw.write("\t\treturn this." + paramMapper + ".deleteBy" + methodName + "(" + paramValueStr + ");");
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

