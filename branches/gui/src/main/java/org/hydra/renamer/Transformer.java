package org.hydra.renamer;

import java.util.Map;

import org.hydra.renamer.item.ClassInfo;

public interface Transformer {
	/**
	 * 转换信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, ClassInfo> transform(Map<String, ClassInfo> map);
}
