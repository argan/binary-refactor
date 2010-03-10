package org.hydra.renamer.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassInfo {
	// internalName like java/lang/String
	public String name;
	public String newName;

	public boolean renamable;

	// superClass + interfaces
	public Set<ClassInfo> parent = new HashSet<ClassInfo>();
	public Set<ClassInfo> children = new HashSet<ClassInfo>();

	public Map<String, MethodInfo> methods = new HashMap<String, MethodInfo>();
	public Map<String, FieldInfo> fields = new HashMap<String, FieldInfo>();

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		return name.equals(((ClassInfo) o).name);
	}

	public int hashCode() {
		return name.hashCode();
	}
}
