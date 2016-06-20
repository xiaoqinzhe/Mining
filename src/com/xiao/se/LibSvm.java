package com.xiao.se;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import libsvm.*;

public class LibSvm {
	svm_train train;
	svm_predict predict;
	String modelPath=null;
	String dataFileName;
	
	
	public LibSvm(){
		train=new svm_train();
		predict=new svm_predict();
	}
	
	
	
	
	public void train(List<List<Double>> data,List<Integer> type,String filename) throws IOException{
		//write to file
		dataFileName=filename;
		writeFormat(data, type, dataFileName);
		System.out.println("main1");
		train.main(new String[]{dataFileName});
		modelPath=dataFileName+".model";
	}
	
	public List<Integer> predict(List<List<Double>> vector,List<Integer> type) throws IOException{
		List<Integer> result=new ArrayList<Integer>();
		if(modelPath==null) return result;
		writeFormat(vector, type, "temp.txt");
		predict.main(new String[]{"temp.txt",modelPath,"result.txt"});
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream("result.txt")));
		String str;
		while((str=in.readLine()) != null){
			result.add(Double.valueOf(str).intValue());
			//System.out.println(str);
		}
		in.close();
		return result;
	}
	
	public void writeFormat(List<List<Double>> data,List<Integer> type,String dataFileName) throws FileNotFoundException{
		PrintWriter out=new PrintWriter(dataFileName);
		for(int i=0;i<data.size();++i){
			List<Double> l=data.get(i);
			out.print(type.get(i));
			for(int j=0;j<l.size();++j){
				out.print(" "+(j+1)+":"+l.get(j));
			}
			out.println();
		}
		out.close();
	}
	
}
