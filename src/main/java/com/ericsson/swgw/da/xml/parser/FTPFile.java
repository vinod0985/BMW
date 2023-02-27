package com.ericsson.swgw.da.xml.parser;

import java.io.Serializable;
import java.util.Calendar;

public class FTPFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5125205137390969468L;
	public static final int FILE_TYPE = 0;
	public static final int DIRECTORY_TYPE = 1;
	public static final int SYMBOLIC_LINK_TYPE = 2;
	public static final int UNKNOWN_TYPE = 3;
	public static final int USER_ACCESS = 0;
	public static final int GROUP_ACCESS = 1;
	public static final int WORLD_ACCESS = 2;
	public static final int READ_PERMISSION = 0;
	public static final int WRITE_PERMISSION = 1;
	public static final int EXECUTE_PERMISSION = 2;
	int _type = 3;
	int _hardLinkCount = 0;
	long _size = 0L;
	String _rawListing = null;
	String _user = null;
	String _group = null;
	String _name = null;
	String _link;
	Calendar _date = null;
	boolean[][] _permissions = new boolean[3][3];

	public void setRawListing(String rawListing) {
		this._rawListing = rawListing;
	}

	public String getRawListing() {
		return this._rawListing;
	}

	public boolean isDirectory() {
		return this._type == 1;
	}

	public boolean isFile() {
		return this._type == 0;
	}

	public boolean isSymbolicLink() {
		return this._type == 2;
	}

	public boolean isUnknown() {
		return this._type == 3;
	}

	public void setType(int type) {
		this._type = type;
	}

	public int getType() {
		return this._type;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getName() {
		return this._name;
	}

	public void setSize(long size) {
		this._size = size;
	}

	public long getSize() {
		return this._size;
	}

	public void setHardLinkCount(int links) {
		this._hardLinkCount = links;
	}

	public int getHardLinkCount() {
		return this._hardLinkCount;
	}

	public void setGroup(String group) {
		this._group = group;
	}

	public String getGroup() {
		return this._group;
	}

	public void setUser(String user) {
		this._user = user;
	}

	public String getUser() {
		return this._user;
	}

	public void setLink(String link) {
		this._link = link;
	}

	public String getLink() {
		return this._link;
	}

	public void setTimestamp(Calendar date) {
		this._date = date;
	}

	public Calendar getTimestamp() {
		return this._date;
	}

	public void setPermission(int access, int permission, boolean value) {
		this._permissions[access][permission] = value;
	}

	public boolean hasPermission(int access, int permission) {
		return this._permissions[access][permission];
	}

	public String toString() {
		return this._rawListing;
	}
}
