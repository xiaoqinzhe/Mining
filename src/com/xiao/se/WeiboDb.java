package com.xiao.se;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WeiboDb {
	Connection connection;
	
	
	
	public WeiboDb(){
		connection = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:weibo.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Opened database successfully");
	}
	
	public void writeBuy(String id,String weibo){
		Statement state;
		try {
			state = connection.createStatement();
			state.executeUpdate("insert into buy values('"+id+"','"+weibo+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeBody(String id,String weibo){
		Statement state;
		try {
			state = connection.createStatement();
			state.executeUpdate("insert into body values('"+id+"','"+weibo+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getWeiboId(){
		Statement state;
		try {
			state = connection.createStatement();
			ResultSet res=state.executeQuery("select * from user;");
			List<String> l=new ArrayList<String>();
			while(res.next()){
				String str=res.getString("id");
				//System.out.println(str);
				l.add(str);
			}
			return l;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getWeiboById(String id){
		Statement state;
		try {
			state = connection.createStatement();
			ResultSet res=state.executeQuery("select * from blog where id='"+id+"';");
			List<String> l=new ArrayList<String>();
			while(res.next()){
				String str=res.getString("blog");
				//System.out.println(str);
				l.add(str);
			}
			return l;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void test(){
		getWeiboId();
	}

	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
