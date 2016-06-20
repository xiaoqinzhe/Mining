package com.xiao.se;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Text {
	String content;
	Map<String,Integer> words;
	int amount;
	int type;
	
	public Text(String str,int type){
		content=str;
		this.type=type;
		words=new HashMap<String,Integer>();
		try {
			List<String> l=SegmentWord.segment(content);
			amount=l.size();
			for(int i=0;i<l.size();++i){
				String temp=l.get(i);
				if(words.containsKey(temp)){
					words.replace(temp, words.get(temp)+1);
					//System.out.println(temp+"  "+words.get(temp));
				}else{
					words.put(temp,1);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//words=new HashSet<String>();
	}
	
	public boolean contains(String word){
		return words.containsKey(word); 
	}
	
	public Set<String> getKeys(){
		return words.keySet();
	}
	
	public int getCount(String word){
		int count=0;
		if(contains(word)){
			count=words.get(word);
		}
		return count;
	}

	@Override
	public String toString() {
		return "Text [content=" + content + ", words=" + words + ", amount=" + amount + ", type=" + type + "]";
	}
	
	
	
}
