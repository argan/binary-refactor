package org.hydra.renamer.item;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
	// internalName like java/lang/String
	public String name;
	public String newName;

	// superClass + interfaces
	public List<ClassInfo> parent = new ArrayList<ClassInfo>();
	public List<ClassInfo> children = new ArrayList<ClassInfo>();

	public List<MethodInfo> methods;
	public List<FieldInfo> fields;

}
