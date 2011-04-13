package org.hydra.renamer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hydra.renamer.Transformer;
import org.hydra.renamer.item.ClassInfo;
import org.hydra.renamer.item.FieldInfo;
import org.hydra.renamer.item.MethodInfo;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DefaultTransformer implements Transformer {

	private static Set<String> keywords = new HashSet<String>(Arrays.asList("abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
			"package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else",
			"import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final",
			"interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"));

	private int id;

	List<Method> methods = new ArrayList<Method>();

	public void method(String owner, String key, String newName) {
		Method me = new Method();
		me.owner = owner;
		me.key = key;
		me.newname = newName;
		methods.add(me);
	}

	private String n(String name, String pre) {
		if (keywords.contains(name)) {
			return String.format("%s%04d_%08d", pre, ++id, name.hashCode() & 0x7FFFFFFF);
		} else {
			return String.format("%s%04d_%s", pre, ++id, name);
		}
	}

	protected Map<String, String> pkgs = new HashMap<String, String>();

	private List<ClassInfo> list = new ArrayList<ClassInfo>();

	private String pkg(String name) {
		String string = pkgs.get(name);
		if (string == null) {
			int a = name.lastIndexOf('/');
			if (a > 0) {
				string = pkg(name.substring(0, a)) + "/" + n(name.substring(a + 1), "p");
			} else {
				string = n(name, "p");
			}
			pkgs.put(name, string);
		}
		return string;
	}

	public void clz(String name, String newName) {
		clzs.put(name, newName);
	}

	public void pkg(String name, String newName) {
		pkgs.put(name, newName);
	}

	protected Map<String, String> clzs = new HashMap<String, String>();

	private String clz(String name) {
		String string = clzs.get(name);
		if (string == null) {
			int a = name.lastIndexOf('$');
			if (a > 0) {
				string = clz(name.substring(0, a)) + '$' + n(name.substring(a + 1), "C");
			} else {
				a = name.lastIndexOf('/');
				if (a > 0) {
					string = pkg(name.substring(0, a)) + "/" + n(name.substring(a + 1), "C");
				} else {
					string = n(name, "C");
				}
			}
			clzs.put(name, string);
		}
		return string;
	}

	public void doAA(ClassInfo classInfo) {
		if (classInfo == null)
			return;
		if (list.contains(classInfo))
			return;
		list.add(classInfo);
		if (!classInfo.name.equals("java/lang/Object")) {
			for (ClassInfo p : classInfo.parent) {
				doAA(p);
			}
		}
	}

	class Method {
		String owner;
		String key;
		String newname;
	}

	public Map<String, ClassInfo> transform(Map<String, ClassInfo> map) {
		for (Map.Entry<String, ClassInfo> entry : map.entrySet()) {
			// String name = entry.getKey();
			ClassInfo classInfo = entry.getValue();
			doAA(classInfo);
		}

		for (Method me : methods) {
			ClassInfo classInfo = map.get(me.owner);
			MethodInfo methodInfo = classInfo.methods.get(me.key);
			for (ClassInfo i : classInfo.parent) {
				String string = rm(i, me.key);
				if (string != null) {
					throw new RuntimeException("无法重命名类:" + me.owner + ",方法:" + me.key + ",到新方法名：" + me.newname + "，请检查依赖关系");
				}
			}
			methodInfo.newName = me.newname;
		}

		for (ClassInfo info : list) {
			if (info.renamable) {
				info.newName = clz(info.name);
			}
			for (FieldInfo f : info.fields.values()) {
				rm(f);
			}

			for (MethodInfo m : info.methods.values()) {
				if (m.newName != null)
					continue;
				if ((m.access & Opcodes.ACC_STATIC) != 0 && m.name.equals("main")) {
					m.newName = m.name;
				} else if ((m.access & Opcodes.ACC_PRIVATE) == 0 || (m.access & Opcodes.ACC_STATIC) == 0) {
					rm(info, m);
				} else {
					rm(m);
				}
			}
		}
		return map;
	}

	private String rm(ClassInfo c, String key) {
		if (c == null || c.name == null)
			return null;
		MethodInfo m = c.methods.get(key);
		if (m == null) {
			for (ClassInfo i : c.parent) {
				String string = rm(i, key);
				if (string != null) {
					return string;
				}
			}
			return null;
		} else if (m.newName != null) {
			return m.newName;
		} else {
			for (ClassInfo i : c.parent) {
				String string = rm(i, key);
				if (string != null) {
					m.newName = string;
					return string;
				}
			}
			if (c.renamable) {
				rm(m);
			} else {
				m.newName = m.name;
			}
			return m.newName;
		}
	}

	private void rm(ClassInfo c, MethodInfo m) {
		String key = "method:" + m.name + m.desc;
		String newName = rm(c, key);
		if (newName == null) {
			m.newName = m.name;
		}
	}

	private void rm(FieldInfo f) {
		String prefix;
		if ((f.access & Opcodes.ACC_STATIC) != 0)
			prefix = "F";
		else
			prefix = "f";
		Type t = Type.getType(f.desc);
		String name = null;
		switch (t.getSort()) {
		case Type.ARRAY:
			name = "A";
			break;
		case Type.BYTE:
			name = "B";
			break;
		case Type.BOOLEAN:
			name = "B";
			break;
		case Type.INT:
			name = "I";
			break;
		case Type.LONG:
			name = "J";
			break;
		case Type.FLOAT:
			name = "F";
			break;
		case Type.DOUBLE:
			name = "D";
			break;
		case Type.OBJECT:
			name = "O";
			break;
		}
		f.newName = n(f.name, prefix + "_" + name + "_");
	}

	private void rm(MethodInfo m) {
		if ((m.access & Opcodes.ACC_STATIC) != 0)
			m.newName = n(m.name, "M");
		else
			m.newName = n(m.name, "m");
	}
}
