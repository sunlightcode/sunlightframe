package SunlightFrame.web;

import java.util.Hashtable;
import java.util.Vector;

public class ConstantCache {
	private static ConstantCache instantce = new ConstantCache();
	private Hashtable<String, Vector<Hashtable<String, String>>> constantHash = new Hashtable<String, Vector<Hashtable<String, String>>>();

	public static ConstantCache getInstance() {
		return instantce;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector<Hashtable<String, String>> getConstantValues(String name) {
		return ((Vector) this.constantHash.get(name));
	}

	public void setConstantValues(String name, Vector<Hashtable<String, String>> constantValues) {
		this.constantHash.put(name, constantValues);
	}

	public void clear() {
		this.constantHash.clear();
	}
}