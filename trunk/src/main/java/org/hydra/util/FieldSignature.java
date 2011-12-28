package org.hydra.util;

public class FieldSignature {
	public FieldSignature(String name2) {
		this.name = name2;
	}

	private String name;
	private int flags;
	private ClassSignature type;
	private Object value;

	public void setFlags(int access) {
		this.flags = access;
	}

	public void setType(ClassSignature classSignature) {
		this.type = classSignature;
	}

	@Override
	public String toString() {
		if (this.ignore) {
			return "FieldSignature [" + name + "]\n";
		}
		return "FieldSignature [name=" + name + ", flags=" + flags + ", type=" + type + ", value=" + value + "]\n";
	}

	public String getLevel0Sig() {
		return Types.qualify(type.getName());
	}

	private boolean ignore = false;
	private String originDesc;

	public boolean isIgnore() {
		return this.ignore;
	}

	public void setIgnore(boolean b) {
		this.ignore = b;
	}

	public String getName() {
		return this.name;
	}

	public void setOriginDesc(String desc) {
		this.originDesc = desc;
	}

	public String getOriginDesc() {
		return this.originDesc;
	}
}
