package com.example.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/demo")
public class DemoController {

	@GetMapping("/export-to-csv")
	private void exportToCsv() {
		Connection con = null;
        String url = "jdbc:mysql://localhost:3306/";
        String db = "doctor_management";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "root";
        FileWriter fw ;
        try{
        Class.forName(driver); 
        con = DriverManager.getConnection(url+db, user, pass);
        Statement st = con.createStatement();

        List<String> tableNameList = new ArrayList<String>();
        
        tableNameList.add("doctors");
         
        //path to the folder where you will save your csv files
        String filename = "C:/csvs/";
         
        //star iterating on each table to fetch its data and save in a .csv file
        for(String tableName:tableNameList)
          {
              int k=0;
               
              int j=1;
               
              System.out.println(tableName);
               
              List<String> columnsNameList  = new ArrayList<String>();
               
              //select all data from table
              ResultSet res = st.executeQuery("select * from "+tableName);
               
              //colunm count is necessay as the tables are dynamic and we need to figure out the numbers of columns
              int colunmCount = getColumnCount(res);
               
               try {
                  fw = new FileWriter(filename+""+tableName+".csv");
                   
                   
                  //this loop is used to add column names at the top of file , if you do not need it just comment this loop
                  for(int i=1 ; i<= colunmCount ;i++)
                  {
                      fw.append(res.getMetaData().getColumnName(i));
                      fw.append(",");
           
                  }
                   
                  fw.append(System.getProperty("line.separator"));
                   
                  while(res.next())
                  {
                      for(int i=1;i<=colunmCount;i++)
                      {
                           
                          //you can update it here by using the column type but i am fine with the data so just converting 
                          //everything to string first and then saving
                          if(res.getObject(i)!=null)
                          {
                          String data= res.getObject(i).toString();
                          fw.append(data) ;
                          fw.append(",");
                          }
                          else
                          {
                              String data= "null";
                              fw.append(data) ;
                              fw.append(",");
                          }
                           
                      }
                      //new line entered after each row
                      fw.append(System.getProperty("line.separator"));
                  }
                   
                   fw.flush();
                    fw.close();
                   
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
               
          }
               
        con.close();
        }
        catch (ClassNotFoundException e){
        System.err.println("Could not load JDBC driver");
        e.printStackTrace();
        }
        catch(SQLException ex){
        System.err.println("SQLException information");
        }
}
   
  
   
//to get numbers of rows in a result set 
public static int  getRowCount(ResultSet res) throws SQLException
{
      res.last();
      int numberOfRows = res.getRow();
      res.beforeFirst();
      return numberOfRows;
}

//to get no of columns in result set
 
public static int  getColumnCount(ResultSet res) throws SQLException
{
    return res.getMetaData().getColumnCount();
}
}
