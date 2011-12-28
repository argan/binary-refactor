package org.hydra.extractor;

import java.util.Arrays;
import java.util.List;

public class Types {
	private static List<String> types = Arrays.asList("void", "boolean", "int", "long", "char", "short", "byte",
	        "double", "float");

	public static boolean isObfucated(String type) {
		return type.startsWith("com.zeroturnaround.javarebel");

	}

	/**
	 * 判断一个类型是否是不会被混淆的类型，例如java.**，int，long等
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isUnchangableType(String type) {

		if (type.startsWith("java") || type.startsWith("javax")) {
			return true;
		}

		if (type.indexOf("[") > 0) {
			type = type.substring(0, type.indexOf("["));
		}

		if (types.contains(type)) {
			return true;
		}
		return false;
	}

	public static String qualify(String type) {
		if (isUnchangableType(type)) {
			return type;
		} else {
			return "?";// 忽略
		}
	}
}
