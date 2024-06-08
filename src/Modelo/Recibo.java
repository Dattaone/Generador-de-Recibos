package Modelo;

public class Recibo {
    private int id;
    private float monto;
    private String nombre;
    private String concepto;
    private String fecha;
    private int dnie;
    private int dnir;

    public Recibo() {
    }

    public Recibo(int id, float monto, String nombre, String concepto, String fecha, int dnie, int dnir) {
        this.id = id;
        this.monto = monto;
        this.nombre = nombre;
        this.concepto = concepto;
        this.fecha = fecha;
        this.dnie = dnie;
        this.dnir = dnir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDnie() {
        return dnie;
    }

    public void setDnie(int dnie) {
        this.dnie = dnie;
    }

    public int getDnir() {
        return dnir;
    }

    public void setDnir(int dnir) {
        this.dnir = dnir;
    }

    
}
