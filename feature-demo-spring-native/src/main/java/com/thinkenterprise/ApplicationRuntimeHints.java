package com.thinkenterprise;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class ApplicationRuntimeHints implements RuntimeHintsRegistrar{

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.resources().registerPattern("templates/*");
		
	}

}
