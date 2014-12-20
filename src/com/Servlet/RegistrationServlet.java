package com.Servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrationServlet extends HttpServlet {
	private String message;

	  public void init() throws ServletException
	  {
	      // Do required initialization
	      message = "Hello World";
	  }

	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	            throws ServletException, IOException
	  {
		  
	      // Set response content type
	      response.setContentType("text/html");

	      // Actual logic goes here.
	      PrintWriter out = response.getWriter();
	      out.println("<h1>" + message + "</h1>");
	  }
	  
	  public void doPost(HttpServletRequest request,
              HttpServletResponse response)
      throws ServletException, IOException
	  {
		  
		  HttpSession session=request.getSession(true);
		  // Set response content type
	      response.setContentType("text/html");
	      
	   // Set to expire far in the past.
	      response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

	      // Set standard HTTP/1.1 no-cache headers.
	      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

	      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
	      response.addHeader("Cache-Control", "post-check=0, pre-check=0");

	      // Set standard HTTP/1.0 no-cache header.
	      response.setHeader("Pragma", "no-cache");
	      
	      

	      // Actual logic goes here.
	      PrintWriter out = response.getWriter();
	      String name=request.getParameter("name");
	      int business=Integer.parseInt(request.getParameter("businessRange"));
	      int opinion=Integer.parseInt(request.getParameter("opinionRange"));
	      int sports=Integer.parseInt(request.getParameter("sportsRange"));
	      int politics=Integer.parseInt(request.getParameter("politicsRange"));
	      int technology=Integer.parseInt(request.getParameter("technologyRange"));
	      int health=Integer.parseInt(request.getParameter("healthRange"));
	      int arts=Integer.parseInt(request.getParameter("artsRange"));
	      int lifestyle=Integer.parseInt(request.getParameter("lifestyleRange"));
	      int food=Integer.parseInt(request.getParameter("foodRange"));
	      int travel=Integer.parseInt(request.getParameter("travelRange"));
	      
	      
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
		        
		        // statements allow to issue SQL queries to the database
		        statement = connection.createStatement();

		        preparedStatement = connection.prepareStatement("insert into  users values (default, ?, ?, ?, ? , ?,?,?,?,?,?,?,default,default,default)");
		        // "myuser, webpage, datum, summary, COMMENTS from userdetails");
		        // parameters start with 1
		        preparedStatement.setString(1, name);
		        preparedStatement.setInt(2, sports);
		        preparedStatement.setInt(3, politics);
		        preparedStatement.setInt(4, business);
		        preparedStatement.setInt(5, opinion);
		        preparedStatement.setInt(6, technology);
		        preparedStatement.setInt(7, health);
		        preparedStatement.setInt(8, arts);
		        preparedStatement.setInt(9, lifestyle);
		        preparedStatement.setInt(10, food);
		        preparedStatement.setInt(11, travel);
		        preparedStatement.executeUpdate();



		        out.println("Registration successfull!");
		        session.setAttribute("username", name);
		      }
		  	catch (MySQLIntegrityConstraintViolationException e)
		  	{
		  		System.out.println("Username already exists!");
		  		out.println("Username already exists!");
		  	}
		  	catch (Exception e) {	  	
		        e.printStackTrace();
		      }
	  		
	  	} else {
	  		System.out.println("Failed to make connection!");
	  	}
	      
	  	
	  }
	  
	  public void destroy()
	  {
	      // do nothing.
	  }
}
