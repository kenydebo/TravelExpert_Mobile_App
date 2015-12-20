/*
 * Author: Filmon Ghezehey
 * Date: Oct-05-2015
 * Purpose: XML WebService for Agents
 */

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Servlet implementation class GetCustomerXML
 */
public class GetAgentsXML extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String customerId;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAgentsXML() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doStuff(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doStuff(request, response);
	}

	private void doStuff(HttpServletRequest request,
			HttpServletResponse response)
	{
		// TODO Auto-generated method stub

	    //if the employee ID was selected, populate the Employee list
		//customerId = request.getParameter("customerid");
	   // if (customerId != null)
	   // {
			response.setContentType("text/xml");
	    	PrintWriter out;
			try {
				 out = response.getWriter();
			     //get the database objects
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//Connection con1 = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orant11g","ictoosd","ictoosd");
				Class.forName("com.mysql.jdbc.Driver");
				Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/travelexperts","root","P@ssw0rd");
				Statement stmt1 = con1.createStatement();
				//ResultSet rs = stmt1.executeQuery("SELECT * FROM customers where CustomerId = " + customerId);
				ResultSet rs = stmt1.executeQuery("SELECT * FROM agents");
			    
				//print the start of the display table and open the first row
				out.println("<Agents>");
				
			    ResultSetMetaData rsmd = rs.getMetaData();
			    while (rs.next())
			    {
			    	out.println("<Agent>");
			    	for (int i=1; i<=rsmd.getColumnCount(); i++)
			        {
			           out.println("<" + rsmd.getColumnName(i) + ">" + rs.getString(i) + "</" + rsmd.getColumnName(i) + ">");
			        }
			    	out.println("</Agent>");
			    }
			    out.println("</Agents>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	//}
}
