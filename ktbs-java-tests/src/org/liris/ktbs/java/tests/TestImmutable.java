package org.liris.ktbs.java.tests;

public class TestImmutable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Integer a = new Integer(1);
		Integer b = new Integer(2);
		Integer c = new Integer(1);
		System.out.println(a==b);
		System.out.println(a==c);
		System.out.println(a.equals(c));
	}

}
