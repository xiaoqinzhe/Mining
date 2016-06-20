package com.xiao.se;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bwaldvogel.liblinear.InvalidInputDataException;

public class Se {
	final static int BODY=0;
	final static int BUY=1;
	final static int BOTH=2;
	final static int NONE=3;
	
	DataMining body;
	DataMining buy;
	WeiboDb db;
	
	public Se(){
		try {
			body=new DataMining(new String[]{"body.txt","bodylabel.txt"},
					new Type(new String[]{"nobody","body"}));
			buy=new DataMining(new String[]{"buy.txt","buylabel.txt"},
					new Type(new String[]{"nobuy","buy"}));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db=new WeiboDb();
	}
	
	public double train(){
		body.train();
		return buy.train();
	}
	
	public void analy(){
		System.out.println("∑÷Œˆ÷–...");
		List<String> ids=db.getWeiboId();
		for(int i=0;i<ids.size();++i){
			String id=ids.get(i);
			List<String> weibos=db.getWeiboById(id);
			for(int j=0;j<weibos.size();++j){
				String weibo=weibos.get(j);
				int t=getType(weibo);
				switch(t){
				case Se.BODY:
					db.writeBody(id, weibo);
					System.out.println(id+":"+weibo);
					break;
				case Se.BUY:
					db.writeBuy(id, weibo);
					System.out.println(id+":"+weibo);
					break;
				case Se.BOTH:
					db.writeBody(id, weibo);
					db.writeBuy(id, weibo);
					System.out.println(id+":"+weibo);
					break;
				case Se.NONE:
					
					break;
				}
				if(t!=Se.NONE) break;
			}
		}
	}
	
	int getType(String str){
		try {
			if(body.predict(str)==1){
				if(buy.predict(str)==1)
					return Se.BOTH;
				else  return Se.BODY;
			}else{
				if(buy.predict(str)==1)
					return Se.BUY;
				else  return Se.NONE;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void main(String args[]){
		/*DataMining dm;
		try {
			dm = new DataMining(new String[]{"weibo.txt","label.txt"},
					new Type(new String[]{"body","nobody"}));
			dm.train("trainbody.txt.model");
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		Se s=new Se();
		s.train();
		s.analy();
		
		/*PrintWriter out;
		try {
			out = new PrintWriter("va.txt");
			Se s=new Se();
			s.train();
			for(int i=100;i<=5000;i+=100){
				System.out.println(i);
				s.dm.setV(i);
				double a=s.train();				
				out.println(i+" "+a);
				
			}out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//s.analy();
		
	}
}
