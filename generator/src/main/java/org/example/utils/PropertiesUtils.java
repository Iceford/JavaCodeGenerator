package org.example.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project: generator
 * @Package: org.example.utils
 * @ClassName: PropertiesUtils
 * @Datetime: 2023/11/01 10:30
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 一个工具类，用于读取和获取属性文件（.properties）中的配置项
 */

public class PropertiesUtils {
    // 用于加载属性文件的内容
    private static final Properties props = new Properties();
    // 用于存储属性文件中的配置项
    private static final Map<String, String> propertiesMap = new ConcurrentHashMap();
    // 静态初始化块：在类加载时执行，用于初始化属性文件的加载和配置项的存储
    static {
        InputStream is = null;
        Reader bf = null;
        try {
            // 获取操作系统类型，并将其转换为大写字母
            String osType = System.getProperty("os.name").toUpperCase();
            // 获取属性文件的路径
            String path = PropertiesUtils.class.getClassLoader().getResource("").getPath();
            // 判断路径中是否包含 ".jar"，如果包含，则表示是在 jar 包中运行，需要读取外部的配置文件
            if (path.contains(".jar")) {
                Integer subIndex = 0;
                // 根据操作系统类型获取适当的路径索引（Windows为6，其他为5）
                if (osType.contains("WINDOWS")) {
                    subIndex = 6;
                } else {
                    subIndex = 5;
                }
                // 截取 jar 包路径并添加 "/application.properties"，创建一个 FileInputStream 对象用于读取外部的配置文件
                path = path.substring(subIndex, path.indexOf(".jar") + 4);
                is = new FileInputStream(new File(path).getParent() + "/application.properties");
                // 如果不是在 jar 包中运行，获取类加载器中的 "application.properties" 文件的输入流
            } else {
                is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            }

            // 将输入流转换为字符流，并指定使用 "utf8" 编码
            bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            // 将属性文件的内容加载到 props 对象中
            props.load(bf);
            // 遍历 props 对象的键集合，将键和对应的值存储到 propertiesMap 中
            Iterator<Object> iterator = props.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                propertiesMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 一个公共静态方法，用于根据配置项的键获取对应的值
    public static String getString(String key) {
        return propertiesMap.get(key);
    }
}
