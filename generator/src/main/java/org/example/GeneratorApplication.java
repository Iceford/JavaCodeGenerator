package org.example;

import org.example.bean.TableInfo;
import org.example.builder.*;
import org.example.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Project: generator
 * @Package: org.example
 * @ClassName: GenerateApplication
 * @Datetime: 2023/11/01 10:28
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 整个程序的入口点，用于生成一些与数据库表对应的 Java 类和相关代码
 */

public class GeneratorApplication {
    // 日志记录器，用于在程序中记录日志
    private static final Logger logger = LoggerFactory.getLogger(GeneratorApplication.class);

    public static void main(String[] args) {

        logger.info("---------------------------------- 开始生成代码 ----------------------------------");
        try {
            // 构建基础类
            BuildBaseJava.execute();
            List<TableInfo> tableInfoList = new BuildTable().getTables();
            for (TableInfo tableInfo : tableInfoList) {
                // 构建bean
                BuildBeanPo.execute(tableInfo);
                // 构建query对象
                BuildBeanQuery.execute(tableInfo);
                // 构建mapper
                BuildMapper.execute(tableInfo);
                // 构建mapper xml
                BuildMapperXml.execute(tableInfo);
                // 构建service
                BuildService.execute(tableInfo);
                // 构建serviceImpl
                BuildServiceImpl.execute(tableInfo);
                // 构建controller
                BuildController.execute(tableInfo);
            }
            logger.info("---------------------------------- 生成代码成功，保存在---> " + PropertiesUtils.getString("path.base"));
        } catch (Exception e) {
            logger.error("生成代码失败，错误信息:", e);
        }
    }
}
