package com.Servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.sun.org.glassfish.external.statistics.annotations.Reset;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static StringBuilder JAVABIN2JSON=new StringBuilder();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
        //String name=(String) request.getAttribute("username");
		JAVABIN2JSON=new StringBuilder();
		HttpSession session =request.getSession();
		String name=(String) session.getAttribute("username");
		String pass="";
		List<String> suggestionResult=new ArrayList<String>();
		JSONArray jArray=new JSONArray();
		PrintWriter out=response.getWriter();
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
	  	
//	  	HttpSession session=request.getSession();
//		String name=(String) session.getAttribute("username");

		try {
	        
	        // statements allow to issue SQL queries to the database
	        statement = connection.createStatement();
	        TreeMap<String, Integer> categoryMap=new TreeMap<String, Integer>();
	        double totalWeight=0;
	        String categoryQuery="select * from users where username='"+name+"'";
	        Statement sta=connection.createStatement();System.out.println(categoryQuery);
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
	        categoryMap.put("nyt", rs.getInt("nyt"));
	        categoryMap.put("rcv", rs.getInt("rcv"));
	        categoryMap.put("wiki", rs.getInt("wiki"));
	        for(Entry<String, Integer> allWeight:categoryMap.entrySet())
	        {
	        	totalWeight=totalWeight+Math.pow(allWeight.getValue(), 2);
	        }
	        totalWeight=Math.sqrt(totalWeight);
	        double sportsWeight=categoryMap.get("sports")/totalWeight;
	        double politicsWeight=categoryMap.get("politics")/totalWeight;
	        double businessWeight=categoryMap.get("business")/totalWeight;
	        double opinionWeight=categoryMap.get("opinion")/totalWeight;
	        double technologyWeight=categoryMap.get("technology")/totalWeight;
	        double healthWeight=categoryMap.get("health")/totalWeight;
	        double artsWeight=categoryMap.get("arts")/totalWeight;
	        double lifestyleWeight=categoryMap.get("lifestyle")/totalWeight;
	        double foodWeight=categoryMap.get("food")/totalWeight;
	        double travelWeight=categoryMap.get("travel")/totalWeight;
	        
	        HttpSolrServer solr = new HttpSolrServer("http://localhost:4501/solr");
	        Integer start = new Integer(0);
			String []fq={"category:politics^"+politicsWeight+",sports^"+sportsWeight+",business^"+businessWeight+",opinion^"+opinionWeight+",technology^"+technologyWeight+",arts^"+artsWeight+",lifestyle^"+lifestyleWeight+",food^"+foodWeight+",travel^"+travelWeight+",health^"+healthWeight};
			String []fq1={"id","title","weblink","copyright","category"};
			String []val={"edismax"};
			String []spell={"/select"};
			String []spellcheck={"on"};
			ModifiableSolrParams params=new ModifiableSolrParams();
		    org.apache.solr.client.solrj.SolrQuery query=new org.apache.solr.client.solrj.SolrQuery();
		    
		    params.set("qt", spell);
		    params.set("spellcheck.build", true);
		    params.set("spellcheck", spellcheck);
		    pass=request.getParameter("searchterms");
		    query.setQuery(pass);
		    query.set("bq",fq[0]);
		    query.set("defType","edismax");
		    query.set("overwrite", false);
		    query.set("spellcheck", true);
		    query.set(CommonParams.QT, spell);
		    query.setQueryType("/select");
		    //query.addFilterQuery(fq);
		    query.setFields(fq1);
		    query.setStart(start) ;
		    
		    System.out.println("serach servler: category weight:"+fq[0]);
		    System.out.println("Search Servlet: firing query:"+query.toString());
		    System.out.println("Serch servlet response");
		    QueryResponse response1 = null;
		    response1 = solr.query(query);
		    if(pass==null || pass.equalsIgnoreCase("*:*"))
		 	   ;
		    else
		    {
		     SpellCheckResponse spellResp=response1.getSpellCheckResponse();
		     List<Collation> collationResults=spellResp.getCollatedResults();
		     if (collationResults != null) {
		       for (Collation collation : collationResults) {
		         for (Correction correction : collation.getMisspellingsAndCorrections()) {
		           String c=correction.getCorrection();
		        	   suggestionResult.add(c);
		         }
		       }
		       String suggestionJSON="";
			    for(String suggestions:suggestionResult)
			    {
			      suggestionJSON=suggestionJSON+suggestions+",";	
			    }
			    suggestionJSON=suggestionJSON.substring(0, suggestionJSON.length()-1);
			    //suggestionJSON=suggestionJSON.concat("]}");
			    System.out.println("Suggestion JSON after improvement is:"+suggestionJSON);
			    //JSONObject suggestionJSONObject=(JSONObject) JSONSerializer.toJSON(suggestionJSON);
			    JSONObject suggestionJSONObject=new JSONObject();
			    suggestionJSONObject.put("suggestions", suggestionJSON);
			    jArray.add(suggestionJSONObject);
			    Iterator<Object> jArrItr=jArray.iterator();
			    JAVABIN2JSON.append(jArrItr.next());
			    System.out.println("Search Servlet:seggestion :"+JAVABIN2JSON);
			    out.println(JAVABIN2JSON);
			    return;
		     }
		    
		        
		    }

		    SolrDocumentList results = response1.getResults();
		    
		    JSONObject highlightjson = new JSONObject(response1.getHighlighting()); 
		    JAVABIN2JSON.append(highlightjson.toString());
		    

		    System.out.println("Search Servlet:Result size:"+results.size());
		    System.out.println("Search Servlet:sending:"+JAVABIN2JSON);
		    out.println(JAVABIN2JSON);
		    
		    JAVABIN2JSON=null;
		    connection.close();
		    categoryMap.clear();
		    highlightjson.clear();
		    jArray.clear();
		    out.close();
		    params.clear();
		    query.clear();
		    results.clear();
		    rs.close();
		    solr.shutdown();
		    sta.close();
		    statement.close();
		    suggestionResult.clear();
		    
		    
		}catch (MySQLIntegrityConstraintViolationException e)
	  	{
	  		System.out.println("Username already exists");
	  	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
