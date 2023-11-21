package org.example.utils;

/**
 * @Project: generator
 * @Package: org.example.utils
 * @ClassName: StringTools
 * @Datetime: 2023/11/01 10:31
 * @Author: HuangRongQuan
 * @Email: rongquanhuang01@gmail.com
 * @Description: 处理字符串的工具类
 */

public class StringTools {
    /**
     * @param field
     * @return String
     * @description 将字符串的首字母转换为大写
     */
    public static String upperCaseFirstLetter(String field) {
        // 判断传入的字符串是否为空，如果为空则直接返回原字符串
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }
        // 判断第二个字母是否为大写，如果是则认为首字母已经是大写，直接返回原字符串
        if (field.length() > 1 && Character.isUpperCase(field.charAt(1))) {
            return field;
        }
        // 将字符串的第一个字符转换为大写，然后将剩余部分拼接起来，返回转换后的字符串
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    /**
     * @param field
     * @return String
     * @description 将字符串的首字母转换为小写
     */
    public static String lowerCaseFirstLetter(String field) {
        // 判断传入的字符串是否为空，如果为空则直接返回原字符串
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            return field;
        }
        // 将字符串的第一个字符转换为小写，然后将剩余部分拼接起来，返回转换后的字符串
        return field.substring(0, 1).toLowerCase() + field.substring(1);
    }

    /**
     * @param args
     * @return void
     * @description 示例方法
     */
    public static void main(String[] args) {
        String tes = "user_id";
        System.out.println(upperCaseFirstLetter(tes));
    }
}
