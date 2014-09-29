package utilitarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import pdvjonatas.Main;

public class bd {

    public Connection conn = null;
    public boolean status;

    public bd() {
    }

    public void connect() {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            conn = DriverManager.getConnection("jdbc:firebirdsql:" + Main.localcliente + ":" + Main.cliente+"?lc_ctype=WIN1252", "SYSDBA", "masterkey");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossivel conectar com o banco de dados. Verifique se o servidor está ativo!");
            System.exit(0);
        }
    }

    public void disconnect() {
        try {
            conn.close();
            status = false;
        } catch (SQLException erro) {
            System.out.println("Erro no fechamento");
            //erro.printStackTrace();
        }
    }

    public boolean isconnected() {
        return status;
    }
}
