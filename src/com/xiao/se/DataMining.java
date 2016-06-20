package com.xiao.se;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bwaldvogel.liblinear.InvalidInputDataException;

public class DataMining {
	String []trainFile;
	Type type;
	protected Features features;
	protected LibSvm svm;
	private Knn knn;
	int k=5;
	LinearLib linear;
	List<Text> lists;
	int trainAmount;
	
	public DataMining(String []file,Type type) throws IOException{
		trainFile=file;
		this.type=type;
		lists=getTexts2(trainFile);
		//80%训练 20 测试		
		trainAmount=(int) (lists.size()*0.9);
		List<Text> trainTexts=new ArrayList<Text>();
		for(int i=0;i<trainAmount;++i){
			trainTexts.add(lists.get(i));
		}
		//处理训练集,特征选择和权重表示
		features=new Features(trainTexts, type);
	}
	
	public double train(){
		return train(null);
	}
	
	public double train(String testFile){
		System.out.println("样本训练(9/1)...");		
		List<List<Double>> trainv=features.getFeatureWeight();
		List<Integer> traintypes=new ArrayList<Integer>();
		for(int i=0;i<trainAmount;++i){
			traintypes.add(lists.get(i).type);
		}		
		linear=new LinearLib();
		try {
			writeFormat(trainv, traintypes,"trainbody.txt" );
			linear.train("trainbody.txt");
			int n1=0,n2=0,a=0,b=0;
			for(int i=trainAmount;i<lists.size();++i){
				List<Double> testv=features.countTfIdf(lists.get(i));
				int c=(int)linear.predict(testv);
				if(lists.get(i).type==0){
					++n1;
					if(c==0)  ++a;
				}else{
					++n2;
					if(c==1)  ++b;
				}
			}
			System.out.println("class 0:"+(double)a/n1);
			System.out.println("class 1:"+(double)b/n2);
			System.out.println(a+"/"+n1+" | "+b+"/"+n2);
			System.out.println("accuary:"+(double)(a+b)/(n1+n2));
			
			//n1=0;n2=0;a=0;b=0;
			knn=new Knn(trainv,traintypes,k);
			/*for(int i=trainAmount;i<lists.size();++i){
				List<Double> testv=features.countTfIdf(lists.get(i));
				int c=(int)knn.predict(testv);
				if(lists.get(i).type==0){
					++n1;
					if(c==0)  ++a;
				}else{
					++n2;
					if(c==1)  ++b;
				}
			}
			System.out.println("class 0:"+(double)a/n1);
			System.out.println("class 1:"+(double)b/n2);
			System.out.println(a+"/"+n1+" | "+b+"/"+n2);
			System.out.println("accuary:"+(double)(a+b)/(n1+n2));*/
			
			return (double)(a+b)/(n1+n2);
			
			
			/*l.predict(predictWithLinear("母婴"));
			l.predict(predictWithLinear("宝宝"));
			l.predict(predictWithLinear("产下"));
			l.predict(predictWithLinear("#MerryChristmas#不想去想了，既然我坚持，而你一直这样子………我知道了…………"));
			l.predict(predictWithLinear("常联系收到地方三个按时到岗广东"));*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidInputDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*List<List<Double>> trainv=new ArrayList<List<Double>>();
		List<List<Double>> testv=new ArrayList<List<Double>>();
		List<Integer> traintypes=new ArrayList<Integer>();
		List<Integer> testtypes=new ArrayList<Integer>();
		for(int i=0;i<trainAmount;++i){
			trainv.add(vector.get(i));
			traintypes.add(lists.get(i).type);
		}
		for(int i=trainAmount;i<vector.size();++i){
			testv.add(vector.get(i));
			testtypes.add(lists.get(i).type);
		}*/
		
		
		return 0;
		
		//arff
		/*PrintWriter out;
		try {
			out = new PrintWriter("weka_body.arff");
			out.println("@relation body");
			List<String> fea=features.getFeatures();
			for(int i=0;i<fea.size();++i){
				out.println("@attribute '"+fea.get(i)+"' real");
			}
			out.println("@attribute "+"'class' "+"{0,1}");
			out.println("@data");
			for(int i=0;i<vector.size();++i){
				List<Double> l=vector.get(i);
				for(int j=0;j<l.size();++j){
					out.print(l.get(j)+",");
				}
				out.println(lists.get(i).type);
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	public void setV(int v){
		features.setFeatureAmount(v);
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
	
	public void test(){
		
	}
	
	public void setModel(String filename){
		svm.modelPath=filename;
	}
	
	public List<Double> predictWithLinear(String content){
		content=preProcessStr(content);
		Text text=new Text(content, 0);
		List<Double> vector = features.countTfIdf(text);
		return vector;
	}
	
	public int predict(String content) throws IOException{
		content=preProcessStr(content);
		Text text=new Text(content, 0);
		List<Double> vector = features.countTfIdf(text);
		return (int)linear.predict(vector);
	}
	
	public int knnPredict(String content){
		content=preProcessStr(content);
		Text text=new Text(content, 0);
		List<Double> vector = features.countTfIdf(text);
		return knn.predict(vector);
	}
	
	public void setKnnK(int k){
		this.k=k;
	}
	
	List<Text> getTexts2(String []strs){
		List<Text> lists=new ArrayList<Text>();
		//获取训练数据集
		try {
			//for(int i=0;i<strs.length;++i){
				BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(strs[0])));
				BufferedReader ain=new BufferedReader(new InputStreamReader(new FileInputStream(strs[1])));
				String str;
				while((str=in.readLine()) != null){
					str=preProcessStr(str);
					int label=Integer.valueOf(ain.readLine());
					if(str.length()>5) lists.add(new Text(str,label));
					//System.out.println(str);
				}
				in.close();
			//}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(lists, new Comparator<Text>() {

			@Override
			public int compare(Text o1, Text o2) {
				// TODO Auto-generated method stub
				if(Math.random()<0.5) return 1;
				else return -1;
			}
		});
		return lists;
	}
	
	List<Text> getTexts(String []strs){
		List<Text> lists=new ArrayList<Text>();
		//获取训练数据集
		try {
			for(int i=0;i<strs.length;++i){
				BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(strs[i])));
				String str;
				while((str=in.readLine()) != null){
					str=preProcessStr(str);
					if(str.length()>5) lists.add(new Text(str,i));
					//System.out.println(str);
				}
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	
	String preProcessStr(String str){
		String reg="\\[.{0,3}\\]";
		str=removeOnePattern(str, reg);
		reg="\\d*";
		str=removeOnePattern(str, reg);
		reg="(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
		str=removeOnePattern(str, reg);
		return str;
	}
	
	String removeOnePattern(String str,String reg){
		Pattern pattern=Pattern.compile(reg);
		Matcher matcher=pattern.matcher(str);
		return matcher.replaceAll("");
	}
		
}
