package com.eardh.gateway.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author eardh
 * @date 2023/3/26 17:20
 */
public class PrimaryType {

    private static Set<String> PRIMARY_TYPE = new HashSet<>();

    static {
        // primitive type
        PRIMARY_TYPE.add(boolean.class.getName());
        PRIMARY_TYPE.add(byte.class.getName());
        PRIMARY_TYPE.add(char.class.getName());
        PRIMARY_TYPE.add(short.class.getName());
        PRIMARY_TYPE.add(int.class.getName());
        PRIMARY_TYPE.add(long.class.getName());
        PRIMARY_TYPE.add(double.class.getName());
        PRIMARY_TYPE.add(float.class.getName());
        // Java base type
        PRIMARY_TYPE.add(String.class.getName());
        PRIMARY_TYPE.add(Byte.class.getName());
        PRIMARY_TYPE.add(Short.class.getName());
        PRIMARY_TYPE.add(Character.class.getName());
        PRIMARY_TYPE.add(Integer.class.getName());
        PRIMARY_TYPE.add(Long.class.getName());
        PRIMARY_TYPE.add(Float.class.getName());
        PRIMARY_TYPE.add(Double.class.getName());
        PRIMARY_TYPE.add(Boolean.class.getName());
        PRIMARY_TYPE.add(Date.class.getName());
        PRIMARY_TYPE.add(Class.class.getName());
        PRIMARY_TYPE.add(BigInteger.class.getName());
        PRIMARY_TYPE.add(BigDecimal.class.getName());
    }


    /*
     * 判断数据是否为原始数据类型
     *
     * @param 类型名
     * @return 结果
     */
    public static boolean isPrimary(String clazz) {
        return PRIMARY_TYPE.contains(clazz);
    }
}
