package com.xiao.se;

import java.util.List;

public class Knn {
	int k;
	List<List<Double>> data;
	List<Integer> type;
	
	public Knn(List<List<Double>> data,List<Integer> type,int k){
		this.data=data;
		this.type=type;
		this.k=k;
	}
	
	public int predict(List<Double> v){
		double min[]=new double[k];
		int index[]=new int[k];
		for(int i=0;i<k;++i){
			min[i]=distance(v,data.get(i));
			index[i]=i;
			for(int j=i;j>0;--j){
				if(min[j]<min[j-1]){
					double t=min[j];
					min[j]=min[j-1];
					min[j-1]=t;
					int w=index[j];
					index[j]=index[j-1];
					index[j-1]=w;
				}else
					break;
			}
		}
		for(int i=k;i<data.size();++i){
			double d=distance(v, data.get(i));
			if(d<min[k-1]){
				min[k-1]=d;
				index[k-1]=i;
				for(int j=k-1;j>0;--j){
					if(min[j]<min[j-1]){
						double t=min[j];
						min[j]=min[j-1];
						min[j-1]=t;
						int w=index[j];
						index[j]=index[j-1];
						index[j-1]=w;
					}else
						break;
				}
			}
		}
		int amount[]=new int[3];
		for(int i=0;i<k;++i){
			amount[type.get(index[i])]++;
			System.out.println("index "+index[i]+" "+min[i]);
		}
		if(amount[0]>amount[1])
			return 0;
		else if(amount[1]>amount[2]) 
			return 1;
		else return 2;
	}
	
	public double distance(List<Double> v1,List<Double> v2){
		double sum=0;
		for(int i=0;i<v1.size();++i){
			sum+=Math.pow(v1.get(i)-v2.get(i), 2);
		}
		return sum;
	}
	
}
