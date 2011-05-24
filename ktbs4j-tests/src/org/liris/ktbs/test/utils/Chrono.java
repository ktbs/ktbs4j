package org.liris.ktbs.test.utils;

public class Chrono {
	public static long measure(Runnable runnable) {
		long start = System.currentTimeMillis();
		runnable.run();
		long end = System.currentTimeMillis();
		return end-start;
	}
}
