/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jonatas
 */
public class monetario {
    Double valorDouble1 = 15.99; //Valor double comum com decimais
    Double valorDouble2 = 0.8; //Valor double comum com decimais
    String valorStr1 = "R$ 21,555"; //Valor no formato da moeda brasileira
    String valorStr2 = "21,555"; //Valor no formato numérico brasileiro
 
    NumberFormat formato1 = NumberFormat.getCurrencyInstance(); //Formato de moeda atual do sistema
    NumberFormat formato2 = NumberFormat.getInstance(); //Formato atual do sistema
    NumberFormat formato3 = NumberFormat.getNumberInstance(); //Formato numérico atual do sistema
    NumberFormat formato4 = NumberFormat.getCurrencyInstance(new Locale("es", "ES")); //Formato de moeda (Espanha)
    NumberFormat formato5 = NumberFormat.getIntegerInstance(); //Formato de inteiro atual do sistema
    NumberFormat formato6 = NumberFormat.getPercentInstance(); //Formato de porcentagem atual do sistema
 
    DecimalFormat formato7 = new DecimalFormat(".##"); //Formato com duas casas decimais depois do ponto (.)
    
    public Number setDouble(String valor){//retorna em double
        Number parse1 = null;
        try {
            parse1 = formato1.parse(valor);
        } catch (ParseException ex) {
            Logger.getLogger(monetario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parse1;
    }
    
}
