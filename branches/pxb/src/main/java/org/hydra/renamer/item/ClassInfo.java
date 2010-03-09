package org.hydra.renamer.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassInfo {
	// internalName like java/lang/String
	public String name;
	public String newName;

	// superClass + interfaces
	public List<ClassInfo> parent = new ArrayList<ClassInfo>();
	public List<ClassInfo> children = new ArrayList<ClassInfo>();

	public Map<String, MethodInfo> methods = new HashMap<String, MethodInfo>();
	public Map<String, FieldInfo> fields = new HashMap<String, FieldInfo>();

}
