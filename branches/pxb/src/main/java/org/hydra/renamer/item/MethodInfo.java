package org.hydra.renamer.item;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
	public String name;
	public String newName;
	public ClassInfo owner;
	public ClassInfo type;
	public ClassInfo ret;
	public List<ClassInfo> args = new ArrayList<ClassInfo>();
}