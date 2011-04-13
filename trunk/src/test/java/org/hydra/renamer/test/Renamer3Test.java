package org.hydra.renamer.test;

import java.util.Map;

import org.hydra.renamer.Renamer;
import org.hydra.renamer.impl.DefaultTransformer;
import org.hydra.renamer.item.ClassInfo;
import org.junit.Test;

public class Renamer3Test {

	@Test
	public void test() throws Exception {
		String[] files = new String[] { "target/test-classes/test.jar" };
		Renamer renamer = new Renamer();
		DefaultTransformer transformer = new DefaultTransformer();
		transformer.clz("org/hydra/Main", "org/hydra/Main");// 保证入口不变
		transformer.pkg("org/hydra", "org/hydra");// 保证包名不变
		transformer.method("org/hydra/TestSuper", "method:show()V", "showx");
		Map<String, ClassInfo> map = renamer.collect(null, files);
		map = transformer.transform(map);
		renamer.action(map, files);
	}

	@Test(expected = Exception.class)
	public void test2() throws Exception {
		String[] files = new String[] { "target/test-classes/test.jar" };
		Renamer renamer = new Renamer();
		DefaultTransformer transformer = new DefaultTransformer();
		transformer.clz("org/hydra/Main", "org/hydra/Main");// 保证入口不变
		transformer.pkg("org/hydra", "org/hydra");// 保证包名不变
		transformer.method("org/hydra/TestObj", "method:show()V", "showx");//!!!
		Map<String, ClassInfo> map = renamer.collect(null, files);
		map = transformer.transform(map);
		renamer.action(map, files);
	}
}
