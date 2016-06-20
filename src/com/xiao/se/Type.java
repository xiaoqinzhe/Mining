package com.xiao.se;

import java.util.List;

public class Type {
	public String[] names;
	
	public Type(String[] names){
		this.names=names;
	}
	
	public int getCount(){
		return names.length;
	}
	
}
