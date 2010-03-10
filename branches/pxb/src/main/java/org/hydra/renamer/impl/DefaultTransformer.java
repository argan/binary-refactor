package org.hydra.renamer.impl;

import java.util.Map;

import org.hydra.renamer.Transformer;
import org.hydra.renamer.item.ClassInfo;

public class DefaultTransformer implements Transformer {

	public Map<String, ClassInfo> transform(Map<String, ClassInfo> map) {
		return map;
	}

}
