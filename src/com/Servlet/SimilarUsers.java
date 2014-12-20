package com.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class SimilarUsers
 * @author Abhishek
 */
@WebServlet("/SimilarUsers")
public class SimilarUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimilarUsers() {
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
		
			String name=(String) session.getAttribute("username");
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
		    PreparedStatement preparedStatement = null;
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
		  	
		//  	HttpSession session=request.getSession();
		//	String name=(String) session.getAttribute("username");
		
		try {
		        
		        // statements allow to issue SQL queries to the database
			
				HashMap<String, Integer> categoryMap=new HashMap<String, Integer>();
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
		        
		        // find similar users
		        
		         categoryQuery="select * from users ";
		         sta=connection.createStatement();System.out.println(categoryQuery);
		        ResultSet similarrs=sta.executeQuery(categoryQuery);
		        double MSE1=258964789.0,MSE2=258964789.0,MSE3=258964789.0,MSE4=258964789.0,MSE5=258964789.0;
		        String[] top5similar= new String[5];
		        double[] top5mse=new double[5];
		        
		        while(similarrs.next()){
		        	//don't include myself
		        	
		        		if(similarrs.getString("username").equals(name))continue;
		      
		        	categoryMap.clear();  
		        	categoryMap.put("sports", similarrs.getInt("sports"));
			        categoryMap.put("politics", similarrs.getInt("politics"));
			        categoryMap.put("business", similarrs.getInt("business"));
			        categoryMap.put("opinion", similarrs.getInt("opinion"));
			        categoryMap.put("technology", similarrs.getInt("technology"));
			        categoryMap.put("health", similarrs.getInt("health"));
			        categoryMap.put("arts", similarrs.getInt("arts"));
			        categoryMap.put("lifestyle", similarrs.getInt("lifestyle"));
			        categoryMap.put("food", similarrs.getInt("food"));
			        categoryMap.put("travel", similarrs.getInt("travel"));
			        
			        totalWeight=0;
			        
			        for(Entry<String, Integer> allWeight:categoryMap.entrySet())
			        {
			        	totalWeight=totalWeight+Math.pow(allWeight.getValue(), 2);
			        }
			        totalWeight=Math.sqrt(totalWeight);
			         if(totalWeight==0)return;
			         
			         	double nsportsWeight=categoryMap.get("sports")/totalWeight;
				        double npoliticsWeight=categoryMap.get("politics")/totalWeight;
				        double nbusinessWeight=categoryMap.get("business")/totalWeight;
				        double nopinionWeight=categoryMap.get("opinion")/totalWeight;
				        double ntechnologyWeight=categoryMap.get("technology")/totalWeight;
				        double nhealthWeight=categoryMap.get("health")/totalWeight;
				        double nartsWeight=categoryMap.get("arts")/totalWeight;
				        double nlifestyleWeight=categoryMap.get("lifestyle")/totalWeight;
				        double nfoodWeight=categoryMap.get("food")/totalWeight;
				        double ntravelWeight=categoryMap.get("travel")/totalWeight;
			        
				        double MSE=Math.pow((nsportsWeight-sportsWeight),2)+Math.pow((npoliticsWeight-politicsWeight),2)+Math.pow((nbusinessWeight-businessWeight),2)+Math.pow((nopinionWeight-opinionWeight),2)+Math.pow((ntechnologyWeight-technologyWeight),2)+Math.pow((nhealthWeight-healthWeight),2)+Math.pow((nartsWeight-artsWeight),2)+Math.pow((nlifestyleWeight-lifestyleWeight),2)+Math.pow((nfoodWeight-foodWeight),2)+Math.pow((ntravelWeight-travelWeight),2);
				    	MSE=Math.sqrt(MSE);
			        
				    	if(MSE<=MSE1){
				    		top5similar[0]=similarrs.getString("username");
				    		top5mse[0]=MSE;
				    		MSE1=MSE;
				    	}else if(MSE<=MSE2){
				    		top5similar[1]=similarrs.getString("username");
				    		top5mse[1]=MSE;
				    		MSE2=MSE;
				    	}else if(MSE<=MSE3){
				    		top5similar[2]=similarrs.getString("username");
				    		top5mse[2]=MSE;
				    		MSE3=MSE;
				    	}else if(MSE<=MSE4){
				    		top5similar[3]=similarrs.getString("username");
				    		top5mse[3]=MSE;
				    		MSE4=MSE;
				    	}else if(MSE<=MSE5){
				    		top5similar[4]=similarrs.getString("username");
				    		top5mse[4]=MSE;
				    		MSE5=MSE;
				    	}
		        }
		        
		        //now update the tables
		        PreparedStatement prepupdate = null;
		        for(int i=0;i<top5similar.length;i++){
			        if(top5similar[i]!=null){	
			        	//insert into new user account the top 5 similar users
			        	preparedStatement = connection.prepareStatement("insert into  similarusers values (default, ?, ?, ?)");      	
			        	preparedStatement.setString(1,name);
			        	preparedStatement.setString(2,top5similar[i]);
			        	preparedStatement.setDouble(3, top5mse[i]);
			        	
			        	preparedStatement.executeUpdate();
			        	System.out.println("similar user: new users insert:"+preparedStatement.toString());
			        	/*********************************************************/
			        	
			        //update the existing user accounts
			        categoryQuery="select * from similarusers where username="+"'"+top5similar[i]+"'" +" ORDER BY mse DESC";
			         sta=connection.createStatement();System.out.println(categoryQuery);
			        ResultSet srs=sta.executeQuery(categoryQuery);
			        
				        if(srs.getFetchSize()==5){
				        	srs.next();
				        	double score=srs.getDouble("mse"); // this is largest MSE for that user
				        	if(top5mse[i]<score){		     
		
				        		prepupdate=connection.prepareStatement("update similarusers set similaruser=?,mse=? where username=? AND mse=?");
				        		prepupdate.setString(1, name);
				        		prepupdate.setDouble(2, top5mse[i]);
				        		prepupdate.setString(3, top5similar[i]);
				        		prepupdate.setDouble(4, score);
				        		prepupdate.executeUpdate();
				        		System.out.println("similar user:update statemnet for "+top5similar[i]+" :"+prepupdate.toString());
				        	}

				        }else{
				        	prepupdate=connection.prepareStatement("insert into similarusers values(default,?,?,?)");
				        	prepupdate.setString(1,  top5similar[i]);
			        		prepupdate.setString(2, name);
			        		prepupdate.setDouble(3, top5mse[i]);
			        		prepupdate.executeUpdate();
			        		System.out.println("similar user:insert statemnet for old user: "+top5similar[i]+" :"+prepupdate.toString());

				        }
			        }
		        }
		        out.println("similarusers updated");
			}catch (MySQLIntegrityConstraintViolationException e)
		  	{
		  		System.out.println("Username already exists");
		  		
		  	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
