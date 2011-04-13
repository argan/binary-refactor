package org.hydra.renamer.test;

import java.util.Map;

import org.hydra.renamer.Renamer;
import org.hydra.renamer.Transformer;
import org.hydra.renamer.impl.DefaultTransformer;
import org.hydra.renamer.item.ClassInfo;
import org.junit.Test;

public class RenamerTest {

	@Test
	public void test() throws Exception {
		String[] files = new String[] { "target/test-classes/test.jar" };
		Renamer renamer = new Renamer();
		Transformer transformer = new DefaultTransformer();
		Map<String, ClassInfo> map = renamer.collect(null, files);
		map = transformer.transform(map);
		renamer.action(map, files);
	}
}
