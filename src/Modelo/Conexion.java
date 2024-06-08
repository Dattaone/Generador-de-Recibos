package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    private Connection con = null;
    private PreparedStatement ps;
    private ResultSet rs;
    private String query;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String user = "root";
    private String pass = "";
    private String host = "localhost";
    private String port = "3306";
    private String db = "dbrecibo";
    private String url = "jdbc:mysql://"+host+":"+port+"/"+db+"?useSSL=false";
    private String jdbcUrl = "jdbc:mysql://localhost:3306/mysql";
    
    public Connection conectar() throws ClassNotFoundException, SQLException{
        prueba();
        Class.forName(driver);
        con = DriverManager.getConnection(url,user,pass);
            query = "SHOW TABLES LIKE 'recibo'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if(!rs.next()){
                query = "create TABLE recibo (" +
                "   id int AUTO_INCREMENT PRIMARY KEY NOT null," +
                "   monto DECIMAL(10,2)," +
                "   nombre varchar(150)," +
                "   concepto varchar(200)," +
                "   fecha date," +
                "   dnie int(8)," +
                "   dnir int(8)" +
                ");";
                try (PreparedStatement stm = con.prepareStatement(query)) {
                    stm.executeUpdate();
                    System.out.println("Tabla 'inventario' creada exitosamente.");
                }
            }
            return con;
    }
    
    public void prueba(){
        try (Connection con = DriverManager.getConnection(jdbcUrl,user,pass)){
            query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1,"dbRecibo");
                rs = ps.executeQuery();
                if(!rs.next()){
                    query = "CREATE DATABASE dbrecibo";
                    try(PreparedStatement stm = con.prepareStatement(query)){
                        stm.executeUpdate();
                        System.out.println("Se creo la base de datos exitosamente...");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void cerrar(){
        try{
            if(ps != null) ps.close();
            if(con!= null) con.close();
            if(rs != null) rs.close();
        }catch(SQLException e){
            System.out.println("Hubo problemas al cerrar la conexion");
            e.printStackTrace();
        }
    }
}
