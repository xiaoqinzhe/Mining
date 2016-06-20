package com.xiao.se;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class SegmentWord {
	private static HashSet<String> stopWords=new HashSet<String>();  //停用词
	
	static{
		//读取停用词
				try {
					BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream("stop_words_ch.txt")));
				String str;
				
					while((str=in.readLine()) != null){
						stopWords.add(str);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public SegmentWord() throws IOException{
		//stopWords=new HashSet<String>();		
	}
	
	public static List<String> segment(String words) throws Exception{
		List<String> lists=new LinkedList<String>();
		String argu = "";
		String system_charset = "GBK";
		int charset_type = 1;
		int init_flag = 0;
		init_flag = CLibrary.Instance.NLPIR_Init(argu
		.getBytes(system_charset), charset_type, "0".getBytes(system_charset));
		if (init_flag==0) {
			System.err.println("初始化失败！");
			throw new Exception("fenci初始化失败");			
		}
		String nativeBytes=CLibrary.Instance.NLPIR_ParagraphProcess(words, 0);
		//System.out.print("fenci提取结果是：" + nativeBytes);
		String[] s=nativeBytes.split(" ");
		for(int i=0;i<s.length;++i)
			lists.add(s[i]);
		CLibrary.Instance.NLPIR_Exit();
		removeStopWords(lists);
		return lists;
	}
	
	protected static void removeStopWords(List<String> lists){
		Iterator<String> it = lists.iterator();
		while(it.hasNext()){
			if(stopWords.contains(it.next())){
				it.remove();
			}
		}
	}
	
	public interface CLibrary extends Library{
		CLibrary Instance = (CLibrary)Native.loadLibrary("NLPIR",CLibrary.class);
		// 初始化函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding,
		byte[] sLicenceCode);
		//执行分词函数声明
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
		//提取关键词函数声明
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
		boolean bWeightOut);
		//退出函数声明
		public void NLPIR_Exit();
	}
}
