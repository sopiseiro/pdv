/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import pdvjonatas.Main;

/**
 *
 * @author Jonatas
 */
public class ImprimiCabecalho {
     public ImprimiCabecalho(){
         
     }
     
     public void getEnderecoCliente(BufferedWriter bw,String codigo){
         try {


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            String sql = "Select * FROM CLIENTE WHERE codigo = "+codigo;
            f.open(sql);
            while (f.next()) {

                bw.write(f.fieldbyname("NOME").toUpperCase() + "\n");
                bw.write(f.fieldbyname("TELEFONE") + "\n");
                bw.write(f.fieldbyname("ENDERECO") + ", " + f.fieldbyname("NUM") +"\n");
                bw.write(f.fieldbyname("BAIRRO")+ ","+ f.fieldbyname("CIDADE").replace("Í", "I") + " - " + f.fieldbyname("UF") + "\n");
                
                
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
     }
     
     public void head(BufferedWriter bw){
         try {


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            
            bw.write(Main.fantasia.toUpperCase() + "\n");
            bw.write(Main.razao_social.toUpperCase() + "\n");
            bw.write(Main.telefone + "\n");
            bw.write(Main.endereco.toUpperCase() + ", " + Main.num +"\n");
            bw.write(Main.cidade + " - " + Main.uf + "\n");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
     }
}
