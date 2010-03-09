package org.hydra.renamer.asm;

import java.util.Map;

import org.hydra.renamer.item.ClassInfo;

public class Remapper extends org.objectweb.asm.commons.Remapper {
	private Map<String, ClassInfo> config;

	public Remapper(Map<String, ClassInfo> config) {
		this.config = config;
	}

	@Override
	public Object mapValue(Object value) {

		// TODO 验证是否可以使用本方法替代ClassForNameFixVisitor
		if (value instanceof String) {
			return value;
		}
		return super.mapValue(value);
	}

	@Override
	public String map(String typeName) {
		ClassInfo classInfo = config.get(typeName);
		if (classInfo != null && classInfo.newName != null)
			return classInfo.newName;
		return typeName;
	}

	@Override
	public String mapFieldName(String owner, String name, String desc) {
		// TODO
		return name;
	}

	@Override
	public String mapMethodName(String owner, String name, String desc) {
		// TODO
		return name;
	}

}
