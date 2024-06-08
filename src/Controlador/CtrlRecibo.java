package Controlador;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Modelo.PdfFont;
import Modelo.Recibo;
import Modelo.Recibo_Dao;
import Vista.frmRecibo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CtrlRecibo implements ActionListener{
    DefaultTableModel modelo = new DefaultTableModel();
    frmRecibo frm = new frmRecibo();
    PdfFont pdfFont = new PdfFont();
    Recibo rc = new Recibo();
    Recibo_Dao dao = new Recibo_Dao();
    ConvertirNumeroALetras nLetras = new ConvertirNumeroALetras();
    List recibo1 = new ArrayList();
    List recibo2 = new ArrayList();
    List recibo3 = new ArrayList();
    boolean btn1 = false;
    boolean btn2 = false;
    boolean btn3 = false;

    public CtrlRecibo(frmRecibo r) {
        this.frm = r;
        this.frm.btnRegistrar.addActionListener(this);
        this.frm.btnEliminar.addActionListener(this);
        this.frm.btnEditar.addActionListener(this);
        this.frm.btnExportar.addActionListener(this);
        this.frm.btnr1.addActionListener(this);
        this.frm.btnr2.addActionListener(this);
        this.frm.btnr3.addActionListener(this);
        this.frm.btnExcel.addActionListener(this);
        listar(frm.tbRecibo);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == frm.btnRegistrar){
            registrar();
            listar(frm.tbRecibo);
        }
        if(e.getSource() == frm.btnEditar){
            actualizar();
            listar(frm.tbRecibo);
        }
        if(e.getSource() == frm.btnEliminar){
            eliminar();
            listar(frm.tbRecibo);
            limpiar();
        }
        if(e.getSource() == frm.btnExportar){
            exportar();
        }
        if(e.getSource() == frm.btnr1){
            recibo1 = exportarDatos();
            if(!btn1){
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/aprobado.png"));
                frm.btnr1.setIcon(iconoInicial);
                btn1 = !btn1;
            }else{
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/recibo.png"));
                frm.btnr1.setIcon(iconoInicial);
                btn1 = !btn1;
            }
        }
        if(e.getSource() == frm.btnr2){
            recibo2 = exportarDatos();
            if(!btn2){
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/aprobado.png"));
                frm.btnr2.setIcon(iconoInicial);
                btn2 = !btn2;
            }else{
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/recibo.png"));
                frm.btnr2.setIcon(iconoInicial);
                btn2 = !btn2;
            }
        }
        if(e.getSource() == frm.btnr3){
            recibo3 = exportarDatos();
                if(!btn3){
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/aprobado.png"));
                frm.btnr3.setIcon(iconoInicial);
                btn3 = !btn3;
            }else{
                ImageIcon iconoInicial = new ImageIcon(getClass().getResource("/Imagenes/recibo.png"));
                frm.btnr3.setIcon(iconoInicial);
                btn3 = !btn3;
            }
        }
        if(e.getSource() == frm.btnExcel){
            exportarExcel();
        }
        
    }
   
    
    public void limpiar(){
        frm.txtConcepto.setText("");
        frm.txtMonto.setText("");
        frm.txtMonto2.setText("");
        frm.txtNombre.setText("");
        frm.txtDnir.setText("");
        frm.txtDnie.setText("");
        frm.txtDia.setText("");
        frm.txtFechaDia.setText("");
        frm.txtFechaMes.setText("");
        frm.txtFechaAño.setText("");
    }
    
    public void limpiarTabla(JTable tabla){
        modelo = (DefaultTableModel)tabla.getModel();
        for(int i = modelo.getRowCount()-1 ; i >= 0 ; i-- ){
            modelo.removeRow(i);
        }
        tabla.setModel(modelo);
    }
    
    public void listar(JTable tabla){
        limpiarTabla(frm.tbRecibo);
        modelo = (DefaultTableModel)tabla.getModel();
        List<Recibo> lista = dao.listar();
        Object[]object = new Object[7];
        for(int i = 0;i < lista.size();i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getMonto();
            object[2] = lista.get(i).getNombre();
            object[3] = lista.get(i).getConcepto();
            object[4] = lista.get(i).getFecha();
            object[5] = lista.get(i).getDnie();
            object[6] = lista.get(i).getDnir();
            modelo.addRow(object);
        }
        int n = dao.serial();
        if(n > 0){
            n += 1;
        }
        String p = n + "";
        while(p.length() <3){
            p = "0"+p;
        }
        frm.lblId.setText("Nº"+ p);
        frm.tbRecibo.setModel(modelo);
    }
    
    public void registrar(){
        float monto = Float.parseFloat(frm.txtMonto.getText());
        String nombre = frm.txtNombre.getText();
        String concepto = frm.txtConcepto.getText();
        String dia = frm.txtFechaDia.getText();
        String mes = frm.txtFechaMes.getText();
        String año = frm.txtFechaAño.getText();
        String fecha = año+"/"+mes+"/"+dia;
        int dnie = Integer.parseInt(frm.txtDnie.getText());
        int dnir = Integer.parseInt(frm.txtDnir.getText());
        
        rc.setMonto(monto);
        rc.setNombre(nombre);
        rc.setConcepto(concepto);
        rc.setFecha(fecha);
        rc.setDnie(dnie);
        rc.setDnir(dnir);
        boolean comprobante = dao.agregar(rc);
        if(!comprobante){
            JOptionPane.showMessageDialog(frm, "Hubo problemas al ingresar los datos.\nrevise silos has colocado correctamente");
        }
    }
    
    public void eliminar(){
        int fila = frm.tbRecibo.getSelectedRow();
        if(fila == -1){
            JOptionPane.showMessageDialog(frm, "Debe seleccionar una Fila");
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(frm, "¿Está seguro de eliminar esta fila?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int codigo = Integer.parseInt(frm.tbRecibo.getValueAt(fila, 0).toString()); 
                dao.eliminar(codigo);
            }
        }
    }
    
    public void actualizar(){
        float monto = Float.parseFloat(frm.txtMonto.getText());
        String nombre = frm.txtNombre.getText();
        String concepto = frm.txtConcepto.getText();
        String dia = frm.txtFechaDia.getText();
        String mes = frm.txtFechaMes.getText();
        String año = frm.txtFechaAño.getText();
        String fecha = año+"/"+mes+"/"+dia;
        int dnie = Integer.parseInt(frm.txtDnie.getText());
        int dnir = Integer.parseInt(frm.txtDnir.getText());
        
        rc.setMonto(monto);
        rc.setNombre(nombre);
        rc.setConcepto(concepto);
        rc.setFecha(fecha);
        rc.setDnie(dnie);
        rc.setDnir(dnir);
        boolean comprobante = dao.actualizar(rc);
        if(!comprobante){
            JOptionPane.showMessageDialog(frm, "Hubo problemas al ingresar los datos.\nrevise silos has colocado correctamente");
        }
    }
    
    public void editar(int filaSeleccionada) {
        if(filaSeleccionada != -1){
            int id = (int) frm.tbRecibo.getValueAt(filaSeleccionada, 0);
            String n = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 0).toString();
            String monto = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 1).toString();
            String nombre = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 2);
            String concepto = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 3);
            String fecha = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 4);
            String dnie = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 5).toString();
            String dnir = (String) frm.tbRecibo.getValueAt(filaSeleccionada, 6).toString();
            
            while(n.length() <3){
                n = "0"+n;
            }
            frm.lblId.setText("Nº"+ n);
            
            rc.setId(id);
            frm.txtDnir.setText(dnir);
            /*
            realizar combo box como en inventario?
            **/
            frm.txtMonto.setText(corregir(monto));
            frm.txtNombre.setText(nombre);
            frm.txtMonto2.setText(nLetras.convertirNumeroALetras(Double.parseDouble(monto)));
            frm.txtConcepto.setText(concepto);
            String[] partes = fecha.split("-");

            if (partes.length == 3) {
                String año = partes[0];
                String mes = partes[1];
                String dia = partes[2];
                frm.txtFechaDia.setText(dia);
                frm.txtFechaMes.setText(mes);
                frm.txtFechaAño.setText(año);
            } else {
                System.err.println("Formato de fecha incorrecto");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dia = LocalDate.parse(fecha, formatter);
            String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            frm.txtDia.setText(nombreDia);
            frm.txtDnie.setText(dnie);
            frm.txtDnir.setText(dnir);
        }
    }
    
    public List exportarDatos(){
        String id = frm.lblId.getText();
        String monto = frm.txtMonto.getText();
        String nombre = frm.txtNombre.getText();
        String cantidad = frm.txtMonto2.getText();
        String concepto = frm.txtConcepto.getText();
        String dia = frm.txtFechaDia.getText();
        String mes = frm.txtFechaMes.getText();
        String año = frm.txtFechaAño.getText();
        String fecha = (dia.isEmpty() || mes.isEmpty() || año.isEmpty()) ? "" : año + "-" + mes + "-" + dia;
        String nombreDia = "";

        try {
            if (!fecha.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate Fecha = LocalDate.parse(fecha, formatter);
                nombreDia = Fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            }
        } catch (Exception e) {
            System.out.println(e);
        }       
        String dnie = frm.txtDnie.getText();
        String dnir =frm.txtDnir.getText();
        
        if(nombre.length() < 54){
            nombre = "  "+nombre;
        }
        
        if(cantidad.length() < 54){
            cantidad = "  "+ cantidad;
        }
        
        if(concepto.length() < 54){
            concepto = "  " + concepto;
        }
        /*
            Transformar los valores float
        */
        
        List<Object> DatosRecibo = new ArrayList<>();
        DatosRecibo.add(monto);
        DatosRecibo.add(nombre);
        DatosRecibo.add(cantidad);
        DatosRecibo.add(concepto);
        DatosRecibo.add(nombreDia);
        DatosRecibo.add(dia);
        DatosRecibo.add(mes);
        DatosRecibo.add(año);
        DatosRecibo.add(dnie);
        DatosRecibo.add(dnir);
        DatosRecibo.add(id);
        
        
        
        return DatosRecibo;
    }
    
    public String corregir(String valor){
        
        String fraccint[] = valor.split("\\.");
        if (fraccint[1].length() >0 && fraccint[1].length() < 2){
            valor = valor + "0";
        }
        return valor;
    }
    
    public void exportar(){
        JFileChooser seleccion = new JFileChooser();
        FileNameExtensionFilter filtro = null;
        String extension = "";
        filtro = new FileNameExtensionFilter("Documento PDF (*.pdf)","pdf");
        
        if(filtro != null){
            seleccion.addChoosableFileFilter(filtro);
        }
        seleccion.setAcceptAllFileFilterUsed(false);
        if(seleccion.showDialog(null,"Exportar Archivo")==JFileChooser.APPROVE_OPTION){
            File archivo = seleccion.getSelectedFile();
            if(!archivo.getName().toLowerCase().endsWith(".pdf")){
                archivo = new File(archivo.getAbsolutePath() + ".pdf");
            }
            if(archivo.exists()){
                int confirmacion = JOptionPane.showConfirmDialog(frm, "¿Estas seguro que quieres sobreescribir\neste archivo?","Sobreescribir archivo",JOptionPane.YES_NO_OPTION);
                if(confirmacion != JOptionPane.YES_OPTION){
                    return;
                }
            }
            Document document = new Document(PageSize.A4);
            
            try{
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(archivo));

                writer.setPageEvent(new PdfPageEventHelper() {
                    @Override
                    public void onStartPage(PdfWriter writer, Document document) {
                        try{
                        }catch(Exception e){
                        
                        }
                    }
                });
                /*
                COMIENZO DE LA HOJA
                */
                
                document.open();
                
                /*
                    COMIENZO DEL RECIBO
                */
                
                if(btn1){
                    document.add(ImprimirRecibo(recibo1));
                }
                document.add(new Phrase("\n",pdfFont.getEspacio()));
                if(btn2){
                    document.add(ImprimirRecibo(recibo2));
                }
                document.add(new Phrase("\n",pdfFont.getEspacio()));
                if(btn3){
                    document.add(ImprimirRecibo(recibo3));
                }
                
                document.close();
                JOptionPane.showMessageDialog(frm, "recibo creado");
            }catch(DocumentException | IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frm, "error al crear el recibo");
            }
        }
    }
    
    static class BordePatronPuntos implements PdfPCellEvent {
        @Override
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            canvas.setLineDash(3, 3, 0);
            canvas.moveTo(position.getLeft(), position.getBottom());
            canvas.lineTo(position.getRight(), position.getBottom());
            canvas.stroke();
            canvas.restoreState();
            
        }
    }
    
    private PdfPTable ImprimirRecibo (List datos) throws DocumentException{
        PdfPTable recibo = new PdfPTable(1);
        recibo.setWidthPercentage(88);

        PdfPCell marco_border = new PdfPCell();
        marco_border.setPadding(12);

        PdfPTable contenedor = new PdfPTable(20);
        contenedor.setWidthPercentage(100);
        contenedor.setWidths(new int[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5});

        /*
            PRIMERA LINEA
        */

        PdfPCell cell_recibo = new PdfPCell();
        cell_recibo.setPhrase(new Phrase("RECIBO  "+datos.get(10),pdfFont.getFontTitulo()));
        cell_recibo.setBorder(0);
        cell_recibo.setColspan(12);
        contenedor.addCell(cell_recibo);

        PdfPCell cell_divisa = new PdfPCell();
        cell_divisa.setPhrase(new Phrase("S/."));
        cell_divisa.setBorder(0);
        cell_divisa.setColspan(1);
        contenedor.addCell(cell_divisa);

        PdfPCell monto = new PdfPCell();
        monto.setHorizontalAlignment(Phrase.ALIGN_RIGHT);
        monto.setColspan(7);
        monto.setPhrase(new Phrase(datos.get(0).toString(),pdfFont.getFontLabel()));
        contenedor.addCell(monto);


        /*
            SEGUNDA LINEA(ESPACIO)
        */
        PdfPCell salto = new PdfPCell();
        for(int i = 0 ; i < 10 ; i++){
            salto.setPhrase(new Phrase("  "));
            salto.setBorder(0);
            salto.setColspan(2);
            contenedor.addCell(salto);
        }

        /*
            TERCERA LINEA
        */

        PdfPCell label = new PdfPCell();
        label.setPhrase(new Phrase("Recibi de ",pdfFont.getFontLabel()));
        label.setColspan(3);
        label.setBorder(0);
        contenedor.addCell(label);

        PdfPCell celda = new PdfPCell();
        celda.setBorder(0);
        celda.setPhrase(new Phrase(datos.get(1).toString(),pdfFont.getFontLabel()));
        celda.setColspan(17);
        celda.setCellEvent(new BordePatronPuntos());
        contenedor.addCell(celda);

        /*
            CUARTA LINEA
        */

        label.setPhrase(new Phrase("la cantidad de",pdfFont.getFontLabel()));
        label.setColspan(4);
        label.setPaddingTop(9);
        contenedor.addCell(label);


        celda.setPhrase(new Phrase(datos.get(2).toString(),pdfFont.getFontLabel()));
        celda.setColspan(16);
        celda.setPaddingTop(9);
        contenedor.addCell(celda);

        /*
            QUINTA LINEA
        */

        label.setPhrase(new Phrase("por concepto de",pdfFont.getFontLabel()));
        label.setColspan(5);
        label.setPaddingTop(8);
        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(3).toString(),pdfFont.getFontLabel()));
        celda.setColspan(15);
        celda.setPaddingTop(9);
        contenedor.addCell(celda);

        /*
            SEXTA LINEA
        */

        label.setPhrase(new Phrase(" "));
        label.setColspan(5);
        label.setPaddingTop(12);
        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(4).toString(),pdfFont.getFontLabel2()));
        celda.setColspan(4);
        celda.setPaddingTop(12);
        celda.setHorizontalAlignment(Phrase.ALIGN_CENTER);
        contenedor.addCell(celda);

        label.setPhrase(new Phrase(",",pdfFont.getFontLabel2()));
        label.setColspan(1);
        label.setHorizontalAlignment(Phrase.ALIGN_CENTER);
        label.setVerticalAlignment(Phrase.ALIGN_BOTTOM);
        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(5).toString(),pdfFont.getFontLabel2()));
        celda.setColspan(2);
        contenedor.addCell(celda);

        label.setPhrase(new Phrase("de",pdfFont.getFontLabel2()));
        label.setColspan(1);
        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(6).toString(),pdfFont.getFontLabel2()));
        contenedor.addCell(celda);

        label.setPhrase(new Phrase("de",pdfFont.getFontLabel2()));
        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(7).toString(),pdfFont.getFontLabel2()));
        celda.setColspan(4);
        contenedor.addCell(celda);

        /*
            SEPTIMA LINEA(ESPACIO)
        */

        for(int i = 0 ; i < 10 ; i++){
            salto.setPhrase(new Phrase("\n"));
            salto.setBorder(0);
            salto.setPaddingTop(18);
            salto.setColspan(2);
            contenedor.addCell(salto);
        }

        /*
            SEPTIMA Y MEDIA LINEA
        */

        label.setHorizontalAlignment(Phrase.ALIGN_LEFT);
        label.setPhrase(new Phrase(" ",pdfFont.getFontLabel3()));
        label.setPaddingTop(30);
        label.setBorder(2);
        label.setColspan(6);
        contenedor.addCell(label);

        salto.setPhrase(new Phrase(" ",pdfFont.getFontLabel3()));
        salto.setColspan(1);
        contenedor.addCell(salto);

        label.setHorizontalAlignment(Phrase.ALIGN_CENTER);
        contenedor.addCell(label);

        contenedor.addCell(salto);

        label.setBorder(0);
        contenedor.addCell(label);


        /*
            OCTAVA LINEA
        */

        label.setHorizontalAlignment(Phrase.ALIGN_LEFT);
        label.setPaddingTop(1);
        label.setPhrase(new Phrase("Recibí conforme ",pdfFont.getFontLabel3()));
        label.setColspan(6);
        contenedor.addCell(label);

        salto.setPhrase(new Phrase("",pdfFont.getFontLabel3()));
        salto.setPaddingTop(1);
        salto.setColspan(1);
        contenedor.addCell(salto);

        label.setHorizontalAlignment(Phrase.ALIGN_LEFT);
        label.setPhrase(new Phrase("Entregué conforme ",pdfFont.getFontLabel3()));
        contenedor.addCell(label);

        contenedor.addCell(salto);

        label.setPhrase(new Phrase("",pdfFont.getFontLabel3()));
        contenedor.addCell(label);

        /*
            NOVENA LINEA
        */

        label.setPhrase(new Phrase("DNI:",pdfFont.getFontLabel3()));
        label.setVerticalAlignment(Phrase.ALIGN_BOTTOM);
        label.setPaddingTop(0);
        label.setColspan(2);
        contenedor.addCell(label);

        celda.setCellEvent(null);
        celda.setPhrase(new Phrase(datos.get(8).toString(),pdfFont.getFontLabel3()));
        celda.setHorizontalAlignment(Phrase.ALIGN_LEFT);
        celda.setVerticalAlignment(Phrase.ALIGN_BOTTOM);
        celda.setPaddingTop(0);
        celda.setColspan(4);
        contenedor.addCell(celda);

        salto.setPadding(0);

        contenedor.addCell(salto);

        contenedor.addCell(label);

        celda.setPhrase(new Phrase(datos.get(9).toString(),pdfFont.getFontLabel3()));
        contenedor.addCell(celda);

        contenedor.addCell(salto);

        celda.setPhrase(new Phrase(""));
        celda.setPaddingTop(0);
        celda.setVerticalAlignment(Phrase.ALIGN_CENTER);
        celda.setColspan(6);
        celda.setBorder(0);
        contenedor.addCell(celda);

        /*
            FINAL RECIBO
        */

        marco_border.addElement(contenedor);
        recibo.addCell(marco_border);
        return recibo;
    }
    
    public void exportarExcel(){
        System.out.println("comenzando exportacion");
        JFileChooser seleccionar = new JFileChooser();
        FileNameExtensionFilter filter = null;
        String extension = "";
        
        System.out.println("seleccionando creo");
        filter = new FileNameExtensionFilter("Libro de Excel (*.xlsx)", "xlsx");
        extension = "xlsx";
        
        if (filter != null) {
            seleccionar.addChoosableFileFilter(filter);
        }
        
        seleccionar.setAcceptAllFileFilterUsed(false);

        if (seleccionar.showDialog(null, "Exportar Archivo") == JFileChooser.APPROVE_OPTION) {
            File archivo = seleccionar.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith("." + extension)) {
                // Añadir la extensión al nombre del archivo si no la tiene
                archivo = new File(archivo.getAbsolutePath() + "." + extension);
            }

            if (archivo.exists()) {
                int confirmacion = JOptionPane.showConfirmDialog(null, "El archivo ya existe. ¿Desea sobrescribirlo?", "Confirmar sobrescritura", JOptionPane.YES_NO_OPTION);

                if (confirmacion != JOptionPane.YES_OPTION) {
                    return; // El usuario no desea sobrescribir, salir del método.
                }
            }
            System.out.println("aqui ya tuviste que elegir");
            int cantFila = frm.tbRecibo.getRowCount();
            int cantColumna = frm.tbRecibo.getColumnCount();
            Workbook wb = new XSSFWorkbook();
            Sheet hoja = wb.createSheet(" "); 

            try {
                // Crear 4 filas vacías
                for (int i = 0; i < 4; i++) {
                    Row fila = hoja.createRow(i);
                    CellStyle estilo = wb.createCellStyle();
                    Font fuente = wb.createFont();
                    fuente.setFontName("Calibri");
                    fuente.setFontHeightInPoints((short) 24);
                    fuente.setBold(true);
                    estilo.setFont(fuente);
                    estilo.setAlignment(HorizontalAlignment.CENTER);

                    if (i == 1) {
                        // Agregar el texto "Año de la Unidad" y fusionar las celdas de A hasta J
                        Cell celda = fila.createCell(0);
                        celda.setCellValue("Año de la Unidad, la Paz y el Desarrollo");
                        celda.setCellStyle(estilo);
                        hoja.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
                    } else if (i == 2) {
                        // Agregar el texto "Recibo" y fusionar las celdas de A hasta J
                        Cell celda = fila.createCell(0);
                        celda.setCellValue("RECIBO DE AÑO-AIP-SECUNDARIA");
                        celda.setCellStyle(estilo);
                        hoja.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
                    } else if (i == 3) {
                        // Agregar el texto "2023" y fusionar las celdas de A hasta J
                        Cell celda = fila.createCell(0);
                        celda.setCellValue("2023");
                        celda.setCellStyle(estilo);
                        hoja.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
                    }
                }

                // Crear una fila para los nombres de las columnas (quinta fila)
                Row headerRow = hoja.createRow(4);
                for (int j = 0; j < cantColumna; j++) {
                    Cell headerCell = headerRow.createCell(j);
                    headerCell.setCellValue(frm.tbRecibo.getColumnName(j));
                }
                //String.valueOf(frm.tbInventario.getColumnName(j))
                // Ahora, agregar los datos de la tabla a partir de la sexta fila
                for (int i = 0; i < cantFila; i++) {
                    Row fila = hoja.createRow(i + 5);
                    for (int j = 0; j < cantColumna; j++) {
                        Cell celda = fila.createCell(j);
                        Object valorCelda = frm.tbRecibo.getValueAt(i, j);
                        if (valorCelda != null) {
                            celda.setCellValue(String.valueOf(valorCelda));
                        } else {
                            celda.setCellType(CellType.BLANK); // Configura la celda como vacía (null).
                        }
                    }
                }
                wb.write(new FileOutputStream(archivo));
                JOptionPane.showMessageDialog(null, "Exportación exitosa");
            } catch (HeadlessException | IOException e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error al exportar el archivo de Excel\nAsegurece de no tener el Excel que quiere reemplazar abierto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}

