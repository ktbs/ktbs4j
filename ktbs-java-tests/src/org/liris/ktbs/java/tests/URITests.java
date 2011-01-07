package org.liris.ktbs.java.tests;

import java.net.URI;
import java.net.URISyntaxException;

public class URITests {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
		URI uri = new URI("http://localhost:8001/base1/model1/Msg");
		System.out.println(uri.getAuthority());
		System.out.println(uri.getHost());
		System.out.println(uri.getFragment());
		System.out.println(uri.getPath());
		System.out.println(uri.getPort());
		System.out.println(uri.getQuery());
		System.out.println(uri.getRawAuthority());
		System.out.println(uri.getRawFragment());
		System.out.println(uri.getRawPath());
		System.out.println(uri.getRawQuery());
		System.out.println(uri.getSchemeSpecificPart());
		System.out.println(uri.getScheme());
		System.out.println(uri.getRawSchemeSpecificPart());
		System.out.println(uri.getRawSchemeSpecificPart());
		System.out.println(uri.getRawUserInfo());
		System.out.println(uri.getUserInfo());
		
	}

}
