package com.xiao.se;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Features {
	List<Text> texts;
	int dataCount;
	Type type;
	int typeCount;
	int typeAmount[];
	double hc;
	List<Sig> allFeature;
	int featureAmount;
	Map<String,Integer> wordf;
	List<String> features;
	List<List<Double>> featuresWeight;
	
	public Features(List<Text> l,Type type){
		texts = l;
		dataCount=texts.size();
		this.type=type;
		typeCount=type.getCount();
		typeAmount=new int[typeCount];
		for(int i=0;i<texts.size();++i){
			typeAmount[texts.get(i).type]++;
		}
		hc=HC();
		wordf=new HashMap<String,Integer>();
		allFeature=new ArrayList<Sig>();
		System.out.println("特征选择...");
		countAllIg();
		System.out.println("特征一共"+allFeature.size()+"维");
		features=new ArrayList<String>();
		featuresWeight=new ArrayList<List<Double>>();
		defaultFeatureSelect();	
		//System.out.println("选取维数3000：");
		System.out.println(features.toString());
		
		PrintWriter out;
		try {
			out = new PrintWriter("debug.txt");
			out.println(featureAmount);
			for(int i=0;i<allFeature.size();++i){
				out.println(i+"  "+allFeature.get(i).toString());
				for(int j=0;j<features.size();++j){
					String key=features.get(j);		
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	private void defaultFeatureSelect() {
		/*int i=0;
		for(i=0;i<allFeature.size();++i){
			if(allFeature.get(i).getIg()<0.0000001)
				break;
		}*/
		setFeatureAmount(4000);
		/*if(allFeature.size()<2000)
			setFeatureAmount(allFeature.size());
		else if(allFeature.size()>2000/0.8)
			featureSelectRate(0.8);
		else setFeatureAmount(2000);*/
	}
	
	public void featureSelectRate(double rate){
		setFeatureAmount((int)(allFeature.size()*rate));
	}

	public List<List<Double>> getFeatureWeight(){
		return featuresWeight;
	}
	
	public void setFeatureAmount(int amount){
		featureAmount=amount;
		System.out.println("选取维数:"+amount);
		features.clear();
		for(int i=0;i<featureAmount;++i){
			features.add(allFeature.get(i).getKey());
		}
		featuresWeight.clear();
		for(int i=0;i<texts.size();++i){
			Text text=texts.get(i);
			List<Double> l=new ArrayList<Double>();
			for(int j=0;j<features.size();++j){
				String key=features.get(j);
				l.add(tf_idf(texts,i,key));				
			}
			//if(i<10) System.out.println(l.toString());
			//if(i==78)System.out.println(l+" "+text.toString());
			featuresWeight.add(l);
		}
		//System.out.println(featuresWeight.get(0).toString());
	}
	
	public List<String> getFeatures(){		
		return features;
	}
	
	public int getFeatureAmount(){
		return featureAmount;
	}
	
	public List<Double> countTfIdf(Text t){
		//System.out.println(t.toString());
		List<Double> l=new ArrayList<Double>();
		Map<String,Integer> newwordf=new HashMap<String, Integer>(wordf);
		Set<String> keys=t.words.keySet();
		Iterator<String> it=keys.iterator();
		while(it.hasNext()){
			String key=it.next();
			newwordf.replace(key, newwordf.get(key));
		}
		for(int j=0;j<features.size();++j){
			String key=features.get(j);
			l.add(tf_idf1(t,key,newwordf));				
		}
		return l;
	}
	
	double tf_idf1(Text t,String key, Map<String, Integer> wordf){
		double tf=0;
		if(t.amount==0) return 0;
		tf=(double)t.getCount(key)/t.amount;
		double idf=Math.log((double)(dataCount+1)/wordf.get(key));
		//if(key.equals("亚马逊")) System.out.println(key+" "+tf+" "+idf+" "+wordf.get(key)+" "+tf*idf);
		return tf*idf;
	}
	
	double tf_idf(List<Text> l,int i,String key){
		double tf=0;
		Text temp=l.get(i);
		if(temp.amount==0) return 0;
		tf=(double)temp.getCount(key)/temp.amount;
		double idf=Math.log((double)dataCount/wordf.get(key));
		//if(i<5&&key.equals("母婴")) System.out.println(key+" "+tf+" "+idf+" "+wordf.get(key)+" "+tf*idf);
		return tf*idf;
	}
	
	public void countAllIg(){
		Map<String,Double> ig=new HashMap<String,Double>();
		for(int i=0;i<texts.size();++i){
			Text temp=texts.get(i);
			Set<String> keys=temp.getKeys();
			Iterator<String> it=keys.iterator();
			while(it.hasNext()){
				String str=it.next();
				if(ig.containsKey(str)){
					
				}else{
					ig.put(str, ig(str));
				}
			}
		}
		//System.out.println(ig.toString());
		//排序
		Set<Entry<String,Double>> igs=ig.entrySet();
		Iterator<Entry<String,Double>> it=igs.iterator();
		while(it.hasNext()){
			Entry<String,Double> en=it.next();
			allFeature.add(new Sig(en.getKey(),en.getValue()));
		}
		Collections.sort(allFeature, new Comparator<Sig>() {

			@Override
			public int compare(Sig o1, Sig o2) {
				if(o1.ig>o2.ig) return -1;
				else if(o1.ig<o2.ig) return 1;
				else return 0;
			}
		});
		//System.out.println(allFeature.size()+" "+allFeature.toString());
	}
	
	double ig(String t){
		return hc-HCT(t);
	}
	
	double HCT(String t){
		int n1=0,n2=0;
		int c1[]=new int[typeCount];
		int c2[]=new int[typeCount];
		for(int i=0;i<typeCount;++i){
			c1[i]=c2[i]=0;
		}		
		for(int i=0;i<texts.size();++i){
			Text temp=texts.get(i);
			if(temp.contains(t)){
				n1++;
				c1[temp.type]++;
			}else{
				n2++;
				c2[temp.type]++;
			}
		}
		if(!wordf.containsKey(t))
			wordf.put(t,n1);
		//if(t.equals("母婴")) System.out.println(t+" "+n1+" "+n2+" "+c1[0]+" "+c1[1]);
		return ((double)n1/dataCount)*hct(n1,c1,typeCount)+
				((double)n2/dataCount)*hct(n2,c2,typeCount);
	}
	
	double hct(int n,int a[],int count){
		double sum=0;
		if(n==0) return 0;
		for(int i=0;i<count;++i){
			sum+=f((double)a[i]/n);
		}
		//System.out.println(sum+" "+n+" "+a[0]+" "+a[1]+" "+count);
		return sum;
	}
	
	double HC(){
		double sum=0;
		for(int i=0;i<typeCount;++i){
			sum+=f(typeAmount[i]/(double)dataCount);
		}
		return sum;
	}
	
	double f(double d){
		if(d==0d) return 0;
		return -d*Math.log(d);
	}
	
	class Sig{
		String key;
		double ig;
		
		
		public Sig(String key, double ig) {
			this.key = key;
			this.ig = ig;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public double getIg() {
			return ig;
		}
		public void setIg(double ig) {
			this.ig = ig;
		}
		@Override
		public String toString() {
			return "Sig [key=" + key + ", ig=" + ig + "]";
		}
		
	}
	
}
