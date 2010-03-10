package org.hydra.renamer.test;

import org.hydra.renamer.impl.DefaultTransformer;

public class Tr3 extends DefaultTransformer {
	{
		pkgs.put("net", "net");
		pkgs.put("net/rim", "net/rim");
		pkgs.put("net/rim/tools", "net/rim/tools");
		pkgs.put("net/rim/tools/compiler", "net/rim/tools/compiler");
	}
	{
		clzs.put("net/rim/tools/compiler/Compiler", "net/rim/tools/compiler/Compiler");
	}

}
