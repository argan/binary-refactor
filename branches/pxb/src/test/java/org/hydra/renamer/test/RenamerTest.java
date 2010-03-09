package org.hydra.renamer.test;

import org.hydra.renamer.Renamer;
import org.junit.Test;

public class RenamerTest {

	@Test
	public void test() throws Exception {
		Renamer.main("target/test-classes/test.jar");
	}
}
