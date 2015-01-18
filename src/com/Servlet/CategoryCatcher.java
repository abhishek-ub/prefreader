package com.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class CategoryCatcher
 * @author Abhishek
 */
@WebServlet("/CategoryCatcher")
public class CategoryCatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryCatcher() {
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
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		
		response.setHeader("Cache-Control", "private,no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
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
		String id=request.getParameter("id");
		String[] ourcat={"sports","politics","business","opinion","technology","health","arts","lifestyle","food","travel","nyt","rcv","wiki"};
		String category = request.getParameter("category");
		try {

		System.out.println("catagory catcher:"+category);	
		if(category.length()>0){
			for(int i=0;i<ourcat.length;i++){
				//System.out.println("catagory catcher: cat:"+ourcat[i]);	
				if(category.toLowerCase().contains(ourcat[i])){
					/***********found the category  increment the hit *************************/
					PreparedStatement updatesqlstatement=(PreparedStatement) connection.prepareStatement("update users set "+ourcat[i]+"=? where username=?");
					updatesqlstatement.setString(2, name);
					String selectq="select "+ourcat[i]+" from users where username='"+name+"'";
					Statement stm=connection.createStatement();
					ResultSet rs=stm.executeQuery(selectq);
					rs.next();

					updatesqlstatement.setInt(1, rs.getInt(ourcat[i])+1);
					updatesqlstatement.executeUpdate();
					System.out.println("catagory catcher:updated table: "+updatesqlstatement.toString());
					/************************************************************************/
					
					/***********************mark the doc id against the user******************/
					selectq="select count(*) from docsread where username='"+name+"'" +" AND docid='"+id+"'";
					stm=connection.createStatement();
					rs=stm.executeQuery(selectq);
					rs.next();
					System.out.println("catagory catcher:count from  "+selectq.toString()+" = "+rs.getInt("count(*)"));
					if(rs.getInt("count(*)")==0){
						PreparedStatement insert=(PreparedStatement) connection.prepareStatement("insert into docsread values(default,?,?)");
						insert.setString(1, name);
						insert.setString(2, id);
						insert.executeUpdate();
						System.out.println("catagory catcher: docread inserted :"+insert.toString());
					}
					
					/************************************************************************/
				}
			}
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		out.println("Got the Category!!!");
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
