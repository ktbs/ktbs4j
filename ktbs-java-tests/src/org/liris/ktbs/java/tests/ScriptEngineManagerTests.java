package org.liris.ktbs.java.tests;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineManagerTests {
	
	private static final String PROGRAM = "function sayHello(name) {" +
			"println('Hello, '+name+' !');" +
			"return 2;" +
			"}";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScriptEngineManager manager = new ScriptEngineManager();
		for(ScriptEngineFactory factory:manager.getEngineFactories()) {
			System.out.println(factory.getLanguageName() + " ("+factory.getLanguageVersion()+")");
		}
		
		ScriptEngine js = manager.getEngineByExtension("js");
		
		try {
			Object value = js.eval(PROGRAM);
			value = ((Invocable)js).invokeFunction("sayHello", 2);
			System.out.println(value);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		

	}

}
