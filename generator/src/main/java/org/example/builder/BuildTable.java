package org.example.builder;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.PropertiesUtils;
import org.example.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: generator
 * @Package: org.example.builder
 * @ClassName: BuildTable
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 从数据库中获取表信息，并将表信息封装成TableInfo对象的列表
 */

public class BuildTable {
    // 用于记录日志信息
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    // 静态的字符串常量，用于执行SQL查询语句
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";
    private static final String SQL_PREFIX_SHOW_FIELDS = "show full fields from ";
    // 用于表示自增字段
    private static final String KEY_AUTO_INCREMENT = "auto_increment";
    // 声明一个静态的字符串常量，用于执行SQL查询语句
    private static final String SQL_PREFIX_SELECT_INDEX = "show index from ";
    // 用于存储数据库连接
    private static Connection conn = null;

    // 用于建立数据库连接
    public BuildTable() throws ClassNotFoundException, SQLException {
        // 使用PropertiesUtils类从配置文件中读取数据库连接信息
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        // 加载数据库驱动程序
        Class.forName(driverName);
        // 建立数据库连接
        conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * @param tableInfo
     * @param fieldInfoMap
     * @return void
     * @description 根据表名查询索引信息，将唯一索引的字段添加到对应的表信息中
     */
    private static void getKeyIndexInfo(TableInfo tableInfo, Map<String, FieldInfo> fieldInfoMap) {
        PreparedStatement ps = null;
        ResultSet results = null;
        try {
            ps = conn.prepareStatement(SQL_PREFIX_SELECT_INDEX + tableInfo.getTableName());
            results = ps.executeQuery();
            // 获取表信息
            while (results.next()) {
                String keyName = results.getString("KEY_NAME");
                int nonUnique = results.getInt("NON_UNIQUE");
                String columnName = results.getString("COLUMN_NAME");
                if (nonUnique == 0) {// unique 唯一索引
                    List<FieldInfo> keyColumnList = tableInfo.getKeyIndexMap().get(keyName);
                    if (null == keyColumnList) {
                        keyColumnList = new ArrayList();
                        tableInfo.getKeyIndexMap().put(keyName, keyColumnList);
                    }
                    keyColumnList.add(fieldInfoMap.get(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != results) {
                try {
                    results.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param tableInfo
     * @param fieldInfoMap
     * @return List<FieldInfo>
     * @description 读取数据库表的字段信息并将其封装成 FieldInfo 对象
     */
    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo, Map<String, FieldInfo> fieldInfoMap) {
        // 创建一个预编译语句对象
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        try {
            ps = conn.prepareStatement(SQL_PREFIX_SHOW_FIELDS + tableInfo.getTableName());
            fieldResult = ps.executeQuery();
            // 用于存储字段信息
            List<FieldInfo> filedInfoList = new ArrayList();
            // 循环遍历结果集 fieldResult，对每个字段进行处理
            while (fieldResult.next()) {
                // 读取字段名
                String field = fieldResult.getString("FIELD");
                // 读取字段类型
                String type = fieldResult.getString("TYPE");
                // 读取额外信息
                String extra = fieldResult.getString("EXTRA");
                // 读取备注
                String comment = fieldResult.getString("COMMENT");
                // 如果字段类型中包含括号 (，则截取括号之前的部分，以去除类型中的长度限制信息
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                // 对字段名进行处理，生成对应的属性名，将结果存储在变量 propertyName 中
                String propertyName = processField(false, field);
                // 对字段类型进行处理，生成对应的Java类型，并将结果存储在变量 javaType 中
                String javaType = processFieldType(type);

                /**
                 * 通过判断字段类型是否属于特定的日期类型、时间类型或 BigDecimal 类型，更新对应的表信息的标志位
                 */

                // 判断是否date类型
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    tableInfo.setHaveDate(true);
                }
                // 判断是否有datetime类型
                if (ArrayUtils.contains(Constants.SQL_DATE_TIIME_TYPES, type)) {
                    tableInfo.setHaveDateTime(true);
                }
                // 判断是否有bigdecimal类型
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
                    tableInfo.setHaveBigDecimal(true);
                }
                // 将 fieldInfo 添加到 filedInfoList 列表中
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(field);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setSqlType(type);
                fieldInfo.setJavaType(javaType);
                fieldInfo.setComment(comment);
                if (KEY_AUTO_INCREMENT.equalsIgnoreCase(extra)) {
                    fieldInfo.setAutoIncrement(true);
                } else {
                    fieldInfo.setAutoIncrement(false);
                }
                logger.info("字段名:{},类型:{}，扩展:{}，备注:{}，Java类型:{},Jave属性名:{}", field, type, extra, comment, javaType,
                        propertyName);
                filedInfoList.add(fieldInfo);
                fieldInfoMap.put(field, fieldInfo);

            }
            return filedInfoList;
        } catch (Exception e) {
            logger.error("读取表属性失败", e);
            throw new RuntimeException("读取表属性失败" + tableInfo.getTableName(), e);
        } finally { // 关闭结果集fieldResult
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) { // 关闭预编译语句对象ps
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param uperCaseFirstLetter
     * @param field
     * @return String
     * @description 对字段名进行处理，将字段名中的下划线分隔的各个单词进行首字母大写的转换，并根据参数 uperCaseFirstLetter
     *              决定是否将第一个单词的首字母大写。处理后的结果作为字符串返回
     */
    private static String processField(Boolean uperCaseFirstLetter, String field) {
        // 用于构建处理后的字段名
        StringBuffer sb = new StringBuffer(field.length());
        // 将传入的字段名 field 转换为小写，并使用下划线 _ 进行分割，得到一个字符串数组 fields，每个元素代表一个单词或部分
        String[] fields = field.toLowerCase().split("_");
        // 根据参数 uperCaseFirstLetter 的值来决定是否将第一个单词的首字母大写
        sb.append(uperCaseFirstLetter ? StringTools.upperCaseFirstLetter(fields[0]) : fields[0]);
        // 使用循环遍历从第二个单词开始的所有单词（i 从 1 开始），将每个单词的首字母大写，并追加到 sb 中
        for (int i = 1, len = fields.length; i < len; i++) {
            sb.append(StringTools.upperCaseFirstLetter(fields[i]));
        }
        // 循环结束后，将 sb 转换为字符串并返回，即返回处理后的字段名
        return sb.toString();
    }

    /**
     * @param type
     * @return String
     * @description 根据传入的字段类型 type，判断其属于哪种类型，并返回对应的Java类型的字符串表示
     */
    private static String processFieldType(String type) {
        // 是否属于整数类型
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
            // 是否属于长整数类型
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
            // 是否属于字符串类型
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
            // 是否属于日期时间类型或日期类型
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIIME_TYPES, type)
                || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
            return "Date";
            // 是否属于十进制类型
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
            return "BigDecimal";
            // 不属于上述任何一种类型，则抛出运行时异常，提示无法识别的类型
        } else {
            throw new RuntimeException("无法识别的类型:" + type);
        }
    }

    /**
     * @param
     * @return List<TableInfo>
     * @description 获取数据库中的表信息
     */
    public List<TableInfo> getTables() {
        PreparedStatement ps = null; // 用于执行SQL查询
        ResultSet tableResults = null; // SQL查询结果
        try {
            // 创建一个PreparedStatement对象，用于执行"show table status"的SQL查询语句
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            // 执行SQL查询语句并将结果存储在ResultSet对象tableResults中
            tableResults = ps.executeQuery();

            // 用于存储表信息
            List<TableInfo> tableInfoList = new ArrayList();
            // 遍历tableResults中的每一行来读取表信息
            while (tableResults.next()) {
                // 获取表名
                String tableName = tableResults.getString("name");
                // 获取表的备注信息
                String tableComment = tableResults.getString("comment");
                // 将表名赋值给变量 beanName，作为生成的Java Bean的名称
                String beanName = tableName;
                // 如果配置文件中设置了 Constants.IGNORE_TABLE_PREFIX 为 true，则执行以下逻辑
                if (Constants.IGNORE_TABLE_PREFIX) {
                    // 使用 tableName.indexOf("_") 查找表名中下划线的位置
                    int index_prefix = tableName.indexOf("_");
                    // 如果找到下划线（index_prefix != -1）
                    if (index_prefix != -1) {
                        // beanName 更新为去除下划线前缀之后的部分
                        beanName = tableName.substring(index_prefix + 1);
                    }
                }
                // 对 beanName 进行进一步处理
                beanName = processField(true, beanName);

                // 将表名、表备注、JavaBean名称等信息设置到tableInfo对象中
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setComment(tableComment);
                tableInfo.setBeanName(beanName);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);
                logger.info("表：{}，备注:{}，JaveBean:{}", tableName, tableComment, beanName);
                // 获取字段信息
                Map<String, FieldInfo> fieldInfoMap = new HashMap();
                List<FieldInfo> fieldInfoList = readFieldInfo(tableInfo, fieldInfoMap);
                tableInfo.setFieldList(fieldInfoList);
                // 读取主键
                getKeyIndexInfo(tableInfo, fieldInfoMap);
                // 将tableInfo对象添加到tableInfoList中
                tableInfoList.add(tableInfo);
            }
            // 返回tableInfoList，即包含数据库中所有表信息的列表
            return tableInfoList;
        } catch (Exception e) {
            logger.error("获取数据库表失败", e);
            throw new RuntimeException("获取数据库表失败");
        } finally { // 释放资源
            if (null != tableResults) {
                try { // 关闭ResultSet对象
                    tableResults.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try { // 关闭PreparedStatement对象
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try { // 关闭数据库连接
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

