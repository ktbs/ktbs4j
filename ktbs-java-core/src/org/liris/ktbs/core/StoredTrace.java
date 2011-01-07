package org.liris.ktbs.core;

public interface StoredTrace extends Trace {
	public void addObsel(Obsel obsel);
	public void removeObsel(String obselURI);

	public String getDefaultSubject();
	public void setDefaultSubject(String string);
}
