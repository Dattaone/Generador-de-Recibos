
package Controlador;

public class ConvertirNumeroALetras {
    
    public String convertirNumeroALetras(double numero) {
        int parteEntera = (int)numero;
        double parteDecimal = numero - parteEntera;
        parteDecimal = (int)(parteDecimal*100);
        String entero = convertirEnteros(parteEntera);
        String decimal = "";
        
        if(parteDecimal > 0){
            decimal = convertirEnteros((int)parteDecimal);
        }
        String resultado = entero;
        if(decimal.length()> 0){
            return resultado + (" con " + decimal + " soles");
        }
        return resultado + " soles";
    }
    
    
    public static String convertirEnteros(int numero){
        String[] unidades = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
        String[] decenas = {"", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
        String[] especiales = {"diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve"};
        String[] especiales2 = {"veinte","veintiuno","veintidós","veintitrés", "veinticuatro", "veinticinco", "veintiséis", "veintisiete", "veintiocho", "veintinueve"};
        String[] miles = {"", "mil", "millón"};

        if (numero < 10) {
            return unidades[numero];
        } else if (numero < 20) {
            return especiales[numero - 10];
        } else if (numero < 30) {
            return especiales2[numero - 20];
        } else if (numero < 100) {
            int decena = numero / 10;
            int unidad = numero % 10;
            return decenas[decena] + (unidad > 0 ? " y " + unidades[unidad] : "");
        } else if (numero < 1000) {
            int centena = numero / 100;
            int resto = numero % 100;
            switch(centena){
                case 1 -> {
                    if(resto >0){
                        return "ciento"+(" " + convertirEnteros(resto));
                    }else{
                        return "cien";
                    }
                }
                case 5 -> {
                    return "quinientos"+(resto > 0 ? " " + convertirEnteros(resto) : "");
                }
                case 7 -> {
                    return "setecientos"+(resto > 0 ? " " + convertirEnteros(resto) : "");
                }
                case 9 -> {
                    return "novecientos"+(resto > 0 ? " " + convertirEnteros(resto) : "");
                }
            }
            return unidades[centena] + "cientos" + (resto > 0 ? " " + convertirEnteros(resto) : "");
        } else if (numero < 1000000) {
            int millar = numero / 1000;
            int resto = numero % 1000;
            if(millar == 1){
                return "mil"+(resto > 0 ? " " + convertirEnteros(resto) : "");
            }
            return convertirEnteros(millar) + " " + miles[1] + (resto > 0 ? " " + convertirEnteros(resto) : "");
        } else {
            return "Número no soportado"; // Puedes extender la lógica para números más grandes si es necesario
        }

    }
}
