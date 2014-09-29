/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pdvjonatas;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 *
 * @author USER
 */
public class mascara {
    JTextField campo;
    public void dinheiro (JTextField c){
        campo = c;
       campo.setDocument( new PlainDocument(){
            @Override
		protected void insertUpdate( DefaultDocumentEvent chng, AttributeSet attr ){
                    super.insertUpdate( chng, attr );
                    try{
                        System.out.println(campo.getText());
                        System.out.print("testa");
                    }catch(NumberFormatException e){
                        //Não é um numero ignore
                         System.out.print("testa");
                    }

		}

	});
    }

}


