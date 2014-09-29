/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itensMenu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import pdvjonatas.Main;
import utilitarios.bd;
import utilitarios.mySql;

/**
 *
 * @author Jonatas & Stefhanie
 */

public class leituraX
{
  public leituraX()
  {
    if (JOptionPane.showConfirmDialog(null, "Será imprimido um leitura X, continuar?") == 0)
      leiturax();
  }

  public void leiturax()
  {
    try
    {
      File printer = new File(Main.printering);
      BufferedWriter bw = new BufferedWriter(new FileWriter(printer));

      bd b = new bd();
      b.connect();
      mySql f = new mySql(b.conn);
      String sql = "Select * FROM EMITENTE";
      f.open(sql);
      while (f.next()) {
        bw.write(f.fieldbyname("FANTASIA").toUpperCase() + "\n");
        bw.write(f.fieldbyname("RAZAO_SOCIAL").toUpperCase() + "\n");
        bw.write(f.fieldbyname("TELEFONE") + "\n");
        bw.write(f.fieldbyname("ENDERECO") + ", " + f.fieldbyname("NUM") + "\n");
        bw.write(f.fieldbyname("CIDADE") + " - " + f.fieldbyname("UF") + "\n");
        bw.write("______________________________________________\n\n");
      }

      String data = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
      String data2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
      sql = "SELECT SUM(VALOR_TOTAL) AS total FROM VENDAS WHERE DATA_EMISSAO ='" + data + "' AND cancelada ='0'";
      //System.out.println(sql);

      f.open(sql);
      String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
      System.out.println("teste aqui");
      bw.write("**********  LEITURA X  ***************\n\n");
      bw.write("Venda dia: " + data2 + " - " + hora + "\n\n");
      bw.write("       ***  Totalizador Geral  ***\n\n");
      while (f.next()) {
        bw.write(" Venda bruta total: " + f.fieldbyname("total") + "\n\n");
      }
      bw.write("\n*****************************\n\n");
      bw.write("Fim da Leitura X\n\n\n\n\n\n\n\n\n\n\n");
      bw.flush();
      bw.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
  }
}