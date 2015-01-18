package com.Servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class HotTopics
 */
public class HotTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public  static String FinalJson;
	public  static StringBuilder resultJSON=new StringBuilder();
	public  static StringBuilder JAVABIN2JSON;
	public  static HashMap<String, String> categoryDB2UI=new HashMap<String, String>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HotTopics() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		PrintWriter out = response.getWriter();
//		out.print("ausjasdjsad");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String name=(String) session.getAttribute("username");System.out.println("Username: "+name);
		if(!session.isNew() && name!=null){
		categoryDB2UI=new HashMap<String, String>();
		StringBuilder resultJSON=new StringBuilder();
		JAVABIN2JSON=new StringBuilder();
		categoryDB2UI.put("politics", "politicians");
		categoryDB2UI.put("sports", "athletes");
		categoryDB2UI.put("business", "business_people");
		categoryDB2UI.put("lifestyle", "people");
		categoryDB2UI.put("technology", "scientists");
		categoryDB2UI.put("travel", "cities");
		categoryDB2UI.put("food", "fast_food_restaurants");
		categoryDB2UI.put("arts", "books");
		categoryDB2UI.put("health", "books");
		categoryDB2UI.put("opinion", "authors");
		
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache, no-store"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", -1); // Proxies.
		
		  PrintWriter out = response.getWriter();
		  
		  //String name=(String) request.getAttribute("username");
		    
		//FileWriter outputJson=new FileWriter(new File("outputJson.json"));
		//BufferedWriter brw=new BufferedWriter(outputJson);
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
		    Statement statement = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		   
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
		  	}
		  	
		  	try {
		        
		        // statements allow to issue SQL queries to the database
		        statement = connection.createStatement();
		        TreeMap<String, Integer> categoryMap=new TreeMap<String, Integer>();
		        
		        String categoryQuery="select * from users where username='"+name+"'";
		        Statement sta=connection.createStatement();
		        ResultSet rs=sta.executeQuery(categoryQuery);
		        rs.next();
		        categoryMap.put("sports", rs.getInt("sports"));
		        categoryMap.put("politics", rs.getInt("politics"));
		        categoryMap.put("business", rs.getInt("business"));
		        categoryMap.put("opinion", rs.getInt("opinion"));
		        categoryMap.put("technology", rs.getInt("technology"));
		        categoryMap.put("health", rs.getInt("health"));
		        categoryMap.put("arts", rs.getInt("arts"));
		        categoryMap.put("lifestyle", rs.getInt("lifestyle"));
		        categoryMap.put("food", rs.getInt("food"));
		        categoryMap.put("travel", rs.getInt("travel"));
		        
		        Set categorySet=categoryMap.keySet();
				Iterator<String> iterator=categorySet.iterator();
				int prev =-1,next=-1;
				String top1 = "";;
				String top2 = "";
				while(iterator.hasNext())
				{
					String key=iterator.next();
					int curr=categoryMap.get(key);
					if(prev<curr){
						next=prev;
						prev=curr;
						top1=key;
					}else if(next<curr){
						next=curr;
						top2=key;
					}
					
					
				}
				String category[]={"athletes","politicians"};
				if(top1.length()!=0){
				category[0]=categoryDB2UI.get(top1);
				}
				if(top2.length()!=0){
				category[1]=categoryDB2UI.get(top2);
		  		}
				for(int i=1;i<=2;i++)
				{
					StringBuilder trendsString=new StringBuilder();	
//					File file=new File("d:\\json files\\f2"+i+".json");
//					FileWriter fw=new FileWriter(file);
					String data = URLEncoder.encode("ajax", "UTF-8") + "=" + 
		            URLEncoder.encode("1", "UTF-8");
//					
					data += "&" + URLEncoder.encode("cid", "UTF-8") + "=" +
					URLEncoder.encode(category[i-1], "UTF-8");
					data += "&" + URLEncoder.encode("geo", "UTF-8") + "=" +
					URLEncoder.encode("US", "UTF-8");
					data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
		            URLEncoder.encode("201410", "UTF-8");
					
					URL url = new URL("http://www.google.com/trends/topcharts/trendingchart");
				    URLConnection conn= url.openConnection();
				    conn.setDoOutput(true);
		      	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				    wr.write(data);
				    wr.flush();
				    
				    // Get the response
				    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				    String line;
				    while ((line = rd.readLine()) != null) {
				        trendsString.append(line);
				        //System.out.println(line);
				    }

//				    fw.close();
				    wr.close();
				    rd.close();

				    parseJSONDoc(trendsString.toString(),out);
				}	    
		  	}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (MySQLIntegrityConstraintViolationException e)
		  	{
		  		
//		  		out.println("Username already exists!!");
		  	}
		  	catch (Exception e) {	  	
		        e.printStackTrace();
		    }
		//TreMap<String, Integer> categoryMap=(TreeMap<String, Integer>) request.getAttribute("categoryMap");
		
		
				
		FinalJson=JAVABIN2JSON.substring(0, JAVABIN2JSON.length());
		System.out.println(FinalJson);
		//System.out.println("And this is the FinalJson object:"+FinalJson.toString());
		String finalOutput=FinalJson.toString().replaceAll("\n", "");
		
		out.println(finalOutput);
		System.out.println("I'm done!");
//		brw.write(finalOutput);
//		brw.close();
//		outputJson.close();
		
		try{
			connection.close();
			out.close();
//			preparedStatement.close();
			FinalJson=null;
			JAVABIN2JSON=null;
			resultJSON=null;
//			request.logout();
//			response.flushBuffer();
//			response.reset();
//			response.resetBuffer();
//			resultSet.close();
			statement.close();
			destroy();
			}catch(Exception e)
			{e.printStackTrace();}
		}
		else
		{
			System.out.println("Logged Out");
			PrintWriter out=response.getWriter();
			session.invalidate();
			out.println("Logged Out");
		}
	}

	public static void parseJSONDoc(String trendsString,PrintWriter out) throws IOException, ParseException, SolrServerException
	{
		
		//FileReader reader = new FileReader(file);
		JSONParser jsonParser = new JSONParser();
		JSONArray jArray =new JSONArray();
		StringBuilder hotTopic=new StringBuilder();
		hotTopic.append("\"");
		JSONObject jsonObject = (JSONObject) jsonParser.parse(trendsString);
		  if(jsonObject.containsKey("data"))
		  {
		   JSONObject jsonChildObject = (JSONObject)jsonObject.get("data");
		   if(jsonChildObject.containsKey("entityList"))
		   {
			JSONArray jsonGrandChildObject=(JSONArray)jsonChildObject.get("entityList");
			Iterator<Object> itr=jsonGrandChildObject.iterator();
			while(itr.hasNext())
			{
				JSONObject obj=(JSONObject) itr.next();
//				String title=(String) obj.get("title");
//				System.out.println("title is:"+title);
				hotTopic.append((String) obj.get("title"));
				hotTopic.append("\" OR \"");
			}

			HttpSolrServer solr = new HttpSolrServer("http://localhost:4501/solr");
	        Integer start = new Integer(0);
			
			String []fq1={"id","title","content","weblink","category","copyright","copyright"};
			String []val={"edismax"};
		    org.apache.solr.client.solrj.SolrQuery query=new org.apache.solr.client.solrj.SolrQuery();
		    
		    System.out.println("Query:"+hotTopic);
		    
		    if(hotTopic.length()>5)	     
		       query.setQuery(hotTopic.toString().substring(0,hotTopic.length()-5).concat("&wt=json"));
		    else
		    	return;
		    
		    
		    query.set("overwrite", false);
		    query.setRows(5);
		    query.set(CommonParams.WT, "json");
		    query.setFields(fq1);
		    query.setStart(start) ;
		    query.setQueryType("/select");
		    query.setHighlight(true);
		    query.setParam("hl", true);
		    query.setParam("hl.fl", "content,title,weblink,category,copyright");
		    //query.set("defType", val);
		   
		    
		    QueryResponse response1;
		    response1 = solr.query(query);
			//System.out.println("Coming here too");
		    SolrDocumentList results = response1.getResults();
		    System.out.println(results.getNumFound());
		    
		    JSONObject highlightjson = new JSONObject(response1.getHighlighting()); 
            JAVABIN2JSON.append(highlightjson.toString().substring(1,highlightjson.toString().length()-1));
            JAVABIN2JSON.append(",");
            out.println(JAVABIN2JSON);
            
            jsonParser.reset();
		    jArray.clear();
		    hotTopic=null;
		    jsonObject.clear();
		    jsonChildObject.clear();
		    jsonGrandChildObject.clear();
		    solr.shutdown();
		    query.clear();
		    results.clear();

	       }
		  }
		 
	 }


}
