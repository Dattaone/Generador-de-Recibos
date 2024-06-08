package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recibo_Dao {
    Conexion conexion = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    
    public List listar(){
        List<Recibo>datos = new ArrayList<>();
        query = "select * from recibo";
        try{
            con = conexion.conectar();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                Recibo rc = new Recibo();
                rc.setId(rs.getInt("id"));
                rc.setMonto(rs.getFloat("monto"));
                rc.setNombre(rs.getString("nombre"));
                rc.setConcepto(rs.getString("concepto"));
                rc.setFecha(rs.getString("fecha"));
                rc.setDnie(rs.getInt("dnie"));
                rc.setDnir(rs.getInt("dnir"));
                datos.add(rc);
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try{
                if(ps != null) ps.close();
                if(con!= null) con.close();
                if(rs != null) rs.close();
            }catch(SQLException e){
                System.out.println("Hubo problemas al cerrar la conexion");
                e.printStackTrace();
            }
        }
        return datos;
    }
    
    public boolean agregar(Recibo rc){
        query = "insert into recibo(monto,nombre,concepto,fecha,dnie,dnir) values (?,?,?,?,?,?)";
        try{
            con = conexion.conectar();
            ps = con.prepareStatement(query);
            ps.setFloat(1, rc.getMonto());
            ps.setString(2, rc.getNombre());
            ps.setString(3, rc.getConcepto());
            ps.setString(4,rc.getFecha());
            ps.setInt(5,rc.getDnie());
            ps.setInt(6,rc.getDnir());
            int i = ps.executeUpdate();
            if (i > 0){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println(e);
            return false;
        }finally{
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
    
    public boolean actualizar (Recibo rc){
        query = "update recibo set monto = ?, nombre = ?, concepto = ?, fecha = ?, dnie = ?, dnir = ? where id = ?";
        try{
            con = conexion.conectar();
            ps = con.prepareStatement(query);
            ps.setFloat(1, rc.getMonto());
            ps.setString(2, rc.getNombre());
            ps.setString(3, rc.getConcepto());
            ps.setString(4, rc.getFecha());
            ps.setInt(5,rc.getDnie());
            ps.setInt(6, rc.getDnir());
            ps.setInt(7, rc.getId());
            
            int i = ps.executeUpdate();
            if (i > 0){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println(e);
            return false;
        }finally{
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
    
    public void eliminar(int id){
        query = "delete from recibo where id = "+id;
        try{
            con = conexion.conectar();
            ps = con.prepareStatement(query);
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }finally{
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
    
    public int serial(){
        query = "SELECT MAX(id) AS id FROM recibo";
        try{
            con = conexion.conectar();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int r = 0;
            while(rs.next()){
                r = rs.getInt(1);
            }
            System.out.println(r);
            return r;
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try{
                if(ps != null) ps.close();
                if(con!= null) con.close();
                if(rs != null) rs.close();
            }catch(SQLException e){
                System.out.println("Hubo problemas al cerrar la conexion");
                e.printStackTrace();
            }
        }
        return 0;
    }
}
