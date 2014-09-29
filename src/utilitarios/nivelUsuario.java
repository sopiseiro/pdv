/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilitarios;

import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import pdvjonatas.Main;


/**
 *
 * @author Jonatas
 */
public class nivelUsuario {
      
    public nivelUsuario(String usuario){
        
    }
    
    public nivelUsuario(){}
    
    public void nivelUser(String user){     
        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        if (user.equals("ADMINISTRADOR") || user.equals("Administrador")){
            Main.useritemDescAcres = true;
            Main.useritemCancelar = true;
            Main.usercupomDescAcres = true;
            Main.usercupomCancelar = true;
            return;
        }
        f.open("SELECT * FROM USER_ACESSO WHERE USUARIO = '"+user+"' AND MODULO = 'Frente de Caixa' ");
        while (f.next()){
            if (f.fieldbyname("acesso").equals("Item - Desconto/Acréscimo"))
                Main.useritemDescAcres = true;
            
            if (f.fieldbyname("acesso").equals("Item - Cancelar"))
                Main.useritemCancelar = true;
            
            if (f.fieldbyname("acesso").equals("Cupom - Desconto/Acréscimo"))
                Main.usercupomDescAcres = true;
            
            if (f.fieldbyname("acesso").equals("Cupom - Cancelar"))
                Main.usercupomCancelar = true;
            
            
        }
    }
    
    public boolean checkAdmin(String check){
      bd b = new bd();
      b.connect();
      mySql f = new mySql(b.conn);
        
      final JTextField usuario = new JTextField(10);
      JPasswordField senha = new JPasswordField(10);
      senha.setEchoChar('*');
      
      JPanel myPanel = new JPanel(new GridLayout(2,1));
      myPanel.add(new JLabel("Usuário:"));
      myPanel.add(usuario);
      //myPanel.add(); // a spacer
      myPanel.add(new JLabel("Senha:"));
      myPanel.add(senha);

      
       usuario.addComponentListener(new ComponentAdapter(){  
            public void componentShown(ComponentEvent ce){  
                usuario.requestFocusInWindow();  
            }  
        }); 

      JLabel label = new JLabel("Usuário sem permissão, acessar como Administrador?");
      
      if (JOptionPane.showConfirmDialog(null, new JPanel().add(label), "Informação", 
              JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
           
          int result = JOptionPane.showConfirmDialog(null, myPanel, 
               "Apenas usuários com acesso são permitidos", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                f.open("SELECT * FROM USUARIOS WHERE usuario = '"+usuario.getText()+"' AND senha ='"+new String(senha.getPassword())+"'");
                if (f.next()){
                    if (usuario.getText().equals("ADMINISTRADOR") || usuario.getText().equals("Administrador"))
                        return true;
                    f.open("SELECT * FROM USER_ACESSO WHERE USUARIO = '"+usuario.getText()+"' AND MODULO = 'Frente de Caixa' AND ACESSO='"+check+"'");
                    if (f.next()){
                        return true;
                    }
                }
            }
      }
        //JOptionPane.showInputDialog("Senha", "User");
        //JOptionPane.showMessageDialog(null, "Usuário sem permissão de acesso");
        return false;
    }
    
    
}
