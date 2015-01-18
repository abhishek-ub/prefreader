package com.Servlet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class CollaborativeServlet
 * @author Abhishek
 */
public class CollaborativeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CollaborativeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session=request.getSession();
		
		// Set to expire far in the past.
		  response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

		  // Set standard HTTP/1.1 no-cache headers.
		  response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		  // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		  response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		  // Set standard HTTP/1.0 no-cache header.
		  response.setHeader("Pragma", "no-cache");
		
			System.out.println("-------- MySQL JDBC Connection Testing ------------");
		      
		  	try {
		  		Class.forName("com.mysql.jdbc.Driver");
		  	} catch (ClassNotFoundException e) {
		  		System.out.println("Where is your MySQL JDBC Driver?");
		  		e.printStackTrace();
		  		return;
		  	}
		  
		  	
		  	System.out.println("MySQL JDBC Driver Registered!");
		  	Connection connection = null;
		    try {
		  		connection = DriverManager
		  		.getConnection("jdbc:mysql://localhost/nreader","news", "123456");
		   
		  	} catch (SQLException e) {
		  		System.out.println("Connection Failed! Check output console");
		  		e.printStackTrace();
		  		return;
		  	}
		   
		  	if (connection != null) {
		  		System.out.println("You made it, take control your database now!");
		  		
		  	} else {
		  		System.out.println("Failed to make connection!");
		  		return;
		  	}
		  	//get the user name
			String name=(String) session.getAttribute("username");
			PrintWriter out=response.getWriter();
			
		  	String simlilaruQuery="select * from similarusers where username='"+name+"'"+" ORDER BY mse";
		  	String docquery;
	        Statement sta;
			try {
				sta = connection.createStatement();
			    ResultSet rs=sta.executeQuery(simlilaruQuery);
			    StringBuilder Finalresult=new StringBuilder();
			    
			    while(rs.next()){
			    	docquery="select * from docsread where username="+"'"+rs.getString("similaruser")+"'"+" ORDER BY id DESC";
			    	Statement sdoc=connection.createStatement();
			    	ResultSet docs=sdoc.executeQuery(docquery);
			    	System.out.println("collaborative servlet: docquery: "+docquery);
			    	
			    	int num=0;
			    	// take 2 latest read article
			    	while(docs.next()){
			    		num++;
			    		if(num>2)break;
					    	//query the solr for these id
					    	HttpSolrServer solr = new HttpSolrServer("http://localhost:4501/solr");
					    	Integer start = new Integer(0);
					    	String []fq1={"id","title","content","weblink","category","copyright"};
							String []val={"edismax"};
						    org.apache.solr.client.solrj.SolrQuery query=new org.apache.solr.client.solrj.SolrQuery();
						    
						    String qterm="id:"+docs.getString("docid");
						    docs.next();
						    query.setQuery(qterm);
						    query.set("overwrite", false);
						    query.setRows(10);
						    query.set(CommonParams.WT, "json");
						    query.setFields(fq1);
						    query.setStart(start) ;
						    query.setQueryType("/select");
						    query.setHighlight(true);
						    query.setParam("hl", true);
						    query.setParam("hl.fl", "id,content,title,weblink,category");
						    
						    System.out.println("collaborative servlet: firing query: "+qterm);
						    QueryResponse response1;
							response1 = solr.query(query);
							
							JSONObject docsjson = new JSONObject(response1.getHighlighting()); 
							Finalresult.append(docsjson.toString().substring(1, docsjson.toString().toString().length()-1));
							Finalresult.append(",");
			    		
			    	}
			    }
			    
			    //send the result
			    System.out.println("collaborative servlet: sending: "+Finalresult.toString());
			    out.println(Finalresult.toString());
			    
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
	}

}
