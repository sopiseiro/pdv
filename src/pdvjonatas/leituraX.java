/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdvjonatas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import utilitarios.ImprimiCabecalho;
import utilitarios.bd;
import utilitarios.mySql;


/**
 *
 * @author AGENCIA GLOBO
 */
public class leituraX {

    public leituraX() {
        if (JOptionPane.showConfirmDialog(null, "Será imprimido um leitura X, continuar?") == 0) {
            leiturax();
        }

    }

    public void leiturax() {
        
        
        try {
            File printer = new File(Main.printering);
            BufferedWriter bw = new BufferedWriter(new FileWriter(printer));


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            String sql = "";
            
            new ImprimiCabecalho().head(bw);

            //leituraX
            String data = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
            String data2 = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            sql = "SELECT SUM(VALOR_TOT_NOTA) AS total FROM VENDAS WHERE DATA_EMISSAO ='" + data + "' AND cancelada ='0'";

            f.open(sql);
            String hora = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            bw.write("**********  LEITURA X  ***************\n\n");
            bw.write("Venda dia: " + data2 + " - " + hora + "\n\n");
            bw.write("       ***  Totalizador Geral  ***\n\n");
            while (f.next()) {
                bw.write(" Venda bruta total: " + f.fieldbyname("total") + "\n\n");
            }
            bw.write("\n*****************************\n\n");
            bw.write("Fim da Leitura X\n\nTenha um BOM DIA!\n\n\n\n\n\n\n\n\n\n\n");
            bw.flush();
            bw.close();
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
