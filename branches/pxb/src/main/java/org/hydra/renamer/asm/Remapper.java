package org.hydra.renamer.asm;

import java.util.Map;

import org.hydra.renamer.item.ClassInfo;
import org.hydra.renamer.item.FieldInfo;
import org.hydra.renamer.item.MethodInfo;

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
		ClassInfo classInfo = config.get(owner);
		if (classInfo != null) {
			FieldInfo field = classInfo.fields.get("field:" + name + desc);
			if (field != null && field.newName != null)
				return field.newName;
		}
		return name;
	}

	@Override
	public String mapMethodName(String owner, String name, String desc) {
		ClassInfo classInfo = config.get(owner);
		if (classInfo != null) {
			MethodInfo method = classInfo.methods.get("method:" + name + desc);
			if (method != null && method.newName != null)
				return method.newName;
		}
		return name;
	}

}
