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
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class PageCreate
 * @author Abhimat
 */
@WebServlet("/PageCreate")
public class PageCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static StringBuilder JAVABIN2JSON=new StringBuilder();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PageCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		HttpSession session = request.getSession();
//		String name = (String)session.getAttribute("username");
		
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
			String []fq1={"id","title","weblink","content","category","copyright"};
			String []val={"edismax"};
			String []spell={"/select"};
			String []spellcheck={"on"};
			ModifiableSolrParams params=new ModifiableSolrParams();
		    org.apache.solr.client.solrj.SolrQuery query=new org.apache.solr.client.solrj.SolrQuery();
		    
		    params.set("qt", spell);
		    params.set("spellcheck.build", true);
		    params.set("spellcheck", spellcheck);
		    //String pass="\"Alex\" AND \"LEIFER\"";
		    pass=request.getParameter("id");
		    query.setQuery("id:"+pass);
		    query.set("overwrite", false);
		    //query.set(CommonParams.QT, "/spell");
		    //query.set(CommonParams.QT, spell);
		    query.set("spellcheck", true);
		    query.setQueryType("/select");
		    query.setFields(fq1);
		    query.setStart(start) ;
		    //query.set("defType", val);

		    System.out.println("Page create:"+query.toString());
		    QueryResponse response1 = null;
		    response1 = solr.query(query);
		    //response1.getH
//		    if(pass==null || pass.equalsIgnoreCase("*:*"))
//		 	   ;
//		    else
//		    {
//		     SpellCheckResponse spellResp=response1.getSpellCheckResponse();
//		     List<Collation> collationResults=spellResp.getCollatedResults();
//		     if (collationResults != null) {
//		       for (Collation collation : collationResults) {
//		         for (Correction correction : collation.getMisspellingsAndCorrections()) {
//		           String c=correction.getCorrection();
////		           if (!suggestionResult.contains(c))  
//		        	   suggestionResult.add(c);
//		         }
//		       }
//		       String suggestionJSON="";
//			    for(String suggestions:suggestionResult)
//			    {
//			      suggestionJSON=suggestionJSON+suggestions+",";	
//			    }
//			    System.out.println("Suggestion JSON is:"+suggestionJSON);
//			    suggestionJSON=suggestionJSON.substring(0, suggestionJSON.length()-1);
//			    //suggestionJSON=suggestionJSON.concat("]}");
//			    System.out.println("Suggestion JSON after improvement is:"+suggestionJSON);
//			    //JSONObject suggestionJSONObject=(JSONObject) JSONSerializer.toJSON(suggestionJSON);
//			    JSONObject suggestionJSONObject=new JSONObject();
//			    suggestionJSONObject.put("suggestions", suggestionJSON);
//			    jArray.add(suggestionJSONObject);
//			    Iterator<Object> jArrItr=jArray.iterator();
//			    JAVABIN2JSON.append(jArrItr.next());
//			    System.out.println("JAVABIN2JSON at this time:"+JAVABIN2JSON);
//			    out.println(JAVABIN2JSON);
//			    return;
//		     }
//		    
//		        
//		    }

		    SolrDocumentList results = response1.getResults();
		    
		    JSONObject highlightjson = new JSONObject(response1.getHighlighting()); 
		    JAVABIN2JSON.append(highlightjson.toString());
		    String title = results.get(0).get("title").toString().trim();
		    String content = results.get(0).get("content").toString().trim();
		    //System.out.println("   "+title+"\n"+content);

		    System.out.println("Result size:"+results.size());
		    response.setContentType("text/html");
			out.println("<!DOCTYPE>");
			out.println("<html lang=\"en\"><head>");
			out.println("<meta charset=\"utf-8\">");
			out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
			out.println("<meta name=\"description\" content=\"\">");
			out.println("<meta name=\"author\" content=\"Sergey Pozhilov (GetTemplate.com)\">");
			out.println("<title>"+title.substring(1,title.length()-1)+"</title>");
			out.println("<link rel=\"shortcut icon\" href=\"images/gt_favicon.png\">");
			out.println("<link rel=\"stylesheet\" media=\"screen\" href=\"http://fonts.googleapis.com/css?family=Open+Sans:300,400,700\">");
			out.println("<link rel=\"stylesheet\" href=\"css/bootstrap.css\">");
			out.println("<link rel=\"stylesheet\" href=\"css/font-awesome.min.css\">");

			out.println("<!-- Custom styles for our template -->");
			out.println("<link rel=\"stylesheet\" href=\"css/bootstrap-theme.css\" media=\"screen\">");
			out.println("<link rel=\"stylesheet\" href=\"css/main.css\">");
			out.println("<!--Custom css -->");
			out.println("<link rel=\"stylesheet\" href=\"css/mystylecss.css\">");
			out.println("<link rel=\"stylesheet\" href=\"css/bootstrap-slider.css\">");
			
			out.println("<link rel=\"//cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.4.5/css/bootstrapvalidator.min.css \">");
			out.println("<script src=\"js/jquery-1.11.1.js\"></script>");
			out.println("<script src=\"js/headroom.min.js\"></script>");
			out.println("<script src=\"js/jQuery.headroom.min.js\"></script>");
			out.println("<script src=\"js/bootstrap.min.js\"></script>");
			out.println("<script src=\"js/bootstrap-slider.js\"></script>");
			out.println("<script src=\"js/custom-slider.js\"></script>");
			out.println("<script src=\"js/template.js\"></script>");
			out.println("<script src=\"js/PerfSubmit.js\"></script>");
			out.println("<script src=\"//cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.4.5/js/bootstrapvalidator.min.js\"></script>");
			out.println("</head>");
			out.println("<body class=\"home\">");
			out.println("<!-- Fixed navbar -->");
			out.println("<div class=\"navbar navbar-inverse navbar-fixed-top headroom animated slideDown\" style=\"min-height:100px; padding-top:25px;\">");
			out.println("<div class=\"container\">");
			out.println("<div class=\"navbar-header\">");
			out.println("<!-- Button for smallest screens -->");
			out.println("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\"><span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> </button>");
			out.println("<a class=\"navbar-brand\"><img src=\"images/logo.png\" alt=\"Progressus HTML5 template\"></a>");
			out.println("</div>");
			out.println("<div class=\"navbar-collapse collapse\">");
			
			out.println("</div><!--/.nav-collapse -->");
			out.println("</div>");
			out.println("</div>"); 
			out.println("<!-- /.navbar -->");
			out.println("<!-- content -->");
			out.println("<div class=\"container text-center\" style=\"width:60%;\">");
			out.println("<br> <br> <br> <br>");

			out.println("<h2 class=\"thin\">"+title.substring(1,title.length()-1)+"</h1></br></br><p class=\"text-muted\">"+content.substring(1,content.length()-1)+"</p>");
			out.println("</div>");
			out.println("<!-- /content -->");
			out.println("</body>");
			out.println("<html>");
		    
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
		
		
		
		//PrintWriter out = response.getWriter();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String name = (String)session.getAttribute("username");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE><html><head><title></title></head><body><h1></h1></body><html>");
	}

}
