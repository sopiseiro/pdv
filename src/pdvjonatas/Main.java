/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pdvjonatas;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import utilitarios.bd;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import utilitarios.mySql;

/**
 *
 * @author TEMP
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static String cliente = "D:\\DATAGES.GDB";
    public static String localcliente = "localhost";
    public static String printering = "localhost";
    public static boolean comanda = false;
    public static String comanda_mesa = "";
    public static String mensagem_empresa = "Agencia Globo Sistemas";
    public static String vendedor = "";
    public static String fechacupom = "extendido";
    public static String modelo = "1";
    public static String serie = "A";
    public static int controlepapel = 5;
    public static boolean cortarpapel = false;
    
    //ITEM NIVEL USER
    public static boolean useritemDescAcres = false;
    public static boolean useritemCancelar = false;
    public static boolean usercupomDescAcres = false;
    public static boolean usercupomCancelar = false;
    public static boolean useroperador = false;
    
    
    //ITEM DADOS EMPRESA
    public static String razao_social = "jonatas me";
    public static String fantasia = "jonatas cardoso";
    public static String telefone = "66 duro";
    public static String endereco = "dauri riva";
    public static String num = "666";
    public static String cidade = "sinop";
    public static String uf = "bh";
    //public static String itemDesc = 0;

    
    public static void main(String[] args) throws BadLocationException, FileNotFoundException, IOException, ParseException {
        
        FileInputStream fin = new FileInputStream(new File("pdv.ini"));
        DataInputStream dis = new DataInputStream(fin);
        int i = 1;
        while (dis.available() != 0) {
            if (i == 1) {
                cliente = dis.readLine();
            } else if (i == 2) {
                localcliente = dis.readLine();
            } else if (i == 3){
                printering = dis.readLine();
            } else if (i == 4){
                comanda = Boolean.parseBoolean(dis.readLine());
            } else if (i == 5){
                mensagem_empresa = dis.readLine();
            }else if (i == 6){
                fechacupom = dis.readLine();
            }else if (i == 7){
                modelo = dis.readLine();//
            }else if (i == 8){
                serie = dis.readLine();//
            }else if (i == 9){
                controlepapel = Integer.parseInt(dis.readLine());
            }else if (i == 10){
                cortarpapel = Boolean.parseBoolean(dis.readLine());
            }else if (i == 11){
                razao_social = dis.readLine();
            }
            else if (i == 12){
                fantasia = dis.readLine();
            }
            else if (i == 13){
                telefone = dis.readLine();
            }
            else if (i == 14){
                endereco = dis.readLine();
            }
            else if (i == 15){
                num = dis.readLine();
            }
            else if (i == 16){
               cidade = dis.readLine();
            }
            else if (i == 17){
               uf = dis.readLine();
            }
            
            i++;

        }
        fin.close();

        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        f.open("SELECT USUARIO FROM USUARIOS");
        if (f.next()){
            frameLogin p = new frameLogin(null, comanda);
            p.setSize(497, 272);
            p.setLocationRelativeTo(null);
            p.setVisible(true);
        }else{
            framePrincipal p = null;
            try {
                p = new framePrincipal();
            } catch (BadLocationException ex) {
                Logger.getLogger(frameLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
           //p.setExtendedState(JFrame.MAXIMIZED_BOTH);
            p.setVisible(true);
            //
            //p.setSize(800, 600);
        }
        //p.setExtendedState(JFrame.MAXIMIZED_BOTH);
        /*framePrincipal p = null;
            try {
                p = new framePrincipal();
            } catch (BadLocationException ex) {
                Logger.getLogger(frameLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            p.setVisible(true);
            //p.setExtendedState(JFrame.MAXIMIZED_BOTH);
            p.setSize(800, 600);*/
        
        

    }

}
