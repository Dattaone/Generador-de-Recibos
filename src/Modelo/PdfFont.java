package Modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;

public class PdfFont {
    
    public Font getFontTitulo(){
        Font fuente = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        return fuente;
    }
    
    public Font getFontLabel(){
        Font fuente = new Font(Font.FontFamily.HELVETICA, 12,Font.NORMAL,BaseColor.BLACK);
        return fuente;
    }
    
    public Font getFontLabel2(){
        Font fuente = new Font(Font.FontFamily.HELVETICA, 11,Font.NORMAL,BaseColor.BLACK);
        return fuente;
    }
    
    public Font getFontLabel3(){
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
        }
        Font fuente = new Font(baseFont,10);
        return fuente;
    }
    
    public Font getFontLabelBold(){
        Font fuente = new Font(Font.FontFamily.HELVETICA, 16,Font.NORMAL,BaseColor.BLACK);
        return fuente;
    }
    
    public Font getEspacio(){
        Font fuente = new Font(Font.FontFamily.HELVETICA, 3,Font.NORMAL,BaseColor.BLACK);
        return fuente;
    }
}
