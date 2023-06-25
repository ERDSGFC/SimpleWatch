package com.qin.simplewatch;

import org.assertj.core.util.Lists;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class NoSpringTest {
    public static void main(String[] args) {
        var a = "aaaa";
        System.out.println(a.getClass().equals(String.class));
        System.out.println(a.getClass().isAssignableFrom(Object.class));
        ArrayList<String> b = new ArrayList<>();
        System.out.println(isArrayType(b.getClass()));
    }
    public static boolean isArrayType(Class<?> valueClass) {
        return valueClass.isArray() || Collection.class.isAssignableFrom(valueClass);
    }
}
