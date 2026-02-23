
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
    private static String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=sp26_demo_prj;encrypt=false;trustServerCertificate=true";
    private static String userDB ="sa"; 
    private static String passDB = "Mailuong@2025";
    
    public static Connection getConnection(){
        Connection con =null; 
        try{
            Class.forName(driverName); 
            con = DriverManager.getConnection(dbURL, userDB, passDB);
        }catch(Exception ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return con;
    }
        public static void main(String[] args){
        try(Connection con=getConnection()){
            if(con!=null) System.out.println("Connect sp26_demo_prj success");
        }catch(Exception ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
