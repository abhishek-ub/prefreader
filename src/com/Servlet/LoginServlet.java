package com.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 // Set response content type
	      response.setContentType("text/html");

	      // Actual logic goes here.
	      PrintWriter out = response.getWriter();
	      out.println("<h1>" + "yo!" + "</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession(true);
		response.setContentType("text/html");
		
		
		
		// Set to expire far in the past.
		  response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

		  // Set standard HTTP/1.1 no-cache headers.
		  response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		  // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		  response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		  // Set standard HTTP/1.0 no-cache header.
		  response.setHeader("Pragma", "no-cache");
		
		
		
		String username = request.getParameter("username");
		
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
		  		try {
			  		 statement = connection.createStatement();
				        // resultSet gets the result of the SQL query
//				        resultSet = statement.executeQuery("select * from users  where username=");
			  		 preparedStatement=connection.prepareStatement("select * from users where username = ?");
			  		 preparedStatement.setString(1, username);
			  		 resultSet=preparedStatement.executeQuery();
			  		 int count=0;
			  		 while(resultSet.next())
			  		 {
			  			 count++;
			  		 }
			  		PrintWriter out = response.getWriter();
				     
			  		 if(count==0)
			  			 out.println("Not Registered?");
			  		 else{
			  			 out.println("Login Successful!");
			  			 
			  			 session.setAttribute("username", username);
//			  			 response.sendRedirect("search.html");
			  		 }
			  	}
			  	catch(Exception e)
			  	{
			  		e.printStackTrace();
			  	}
		  		
		  	} else {
		  		System.out.println("Failed to make connection!");
		  	}
		      
		  	
	}

}
