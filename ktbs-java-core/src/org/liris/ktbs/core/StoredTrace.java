package org.liris.ktbs.core;

public interface StoredTrace extends Trace {
	public void addObsel(Obsel obsel);
	public String getDefaultSubject();
}