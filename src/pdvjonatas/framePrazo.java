/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * framePrazo.java
 *
 * Created on 28/06/2011, 14:51:28
 */

package pdvjonatas;

//ver depois e remover inuties
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.PlainDocument;
import java.io.File;
import java.io.BufferedWriter;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import utilitarios.NumeroDocument;
import utilitarios.NumeroDocumentJtabel;

/**
 *
 * @author AGENCIA GLOBO
 */
public class framePrazo extends javax.swing.JDialog {

    public String nome, cpf,codigo;
    public DecimalFormat df;
    JTextField j;
    private boolean sucesso = false;
    /** Creates new form framePrazo */
    public framePrazo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        vparcelas.setSelectionMode(0);
        df = new DecimalFormat("###,##0.00");
        //total.setText("8.500,00");
       /* System.out.println("prazo "+total.getText());
        String aux = total.getText().replace(".", "");
        float totalcompra = Float.parseFloat(aux.replace(",", "."));
        
        j = new JTextField();
        j.setHorizontalAlignment(j.CENTER);
        j.setDocument(new NumeroDocumentJtabel(15,vparcelas,totalcompra));
        
        
        vparcelas.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(j));*/
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        vparcelas.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        
              

        vparcelas.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, 0), "enter_jtable");
        vparcelas.getActionMap().put("enter_jtable", new AbstractAction() {
             public void actionPerformed(ActionEvent e) {
                System.out.println("oiieee");
             }
	});
        
    }
    
    public class EditarCelulaTabela extends AbstractCellEditor implements TableCellEditor {  
         // Este é o componente que vai segurar o valor da edicao da celula  
         JComponent component = new JTextField();  
       
          //Este metodo é chamado quando a celula é editada pelo usuario.    
         public Component getTableCellEditorComponent(JTable table, Object value,  
                 boolean isSelected, int rowIndex, int vColIndex) {  
             //'value'é o valor contido na celula que esta na localizacao ((rowIndex, vColIndex)  
                
             if (isSelected)  
             {  
                 // Celula (e talvez outras celulas) sao selecionadas  
             }  
             // Configura o componente com o valor Especificado  
             ((JTextField)component).setText((String)value);  
       
             // Retorna a configuracao do componente  
             return component;  
         }  
       
       
         // Esse metodo é chamado quando é Terminado de Editar a Celula  
         // Ela retorna o novo valor da Celula.  
         public Object getCellEditorValue() {  
           
              //Chama a funcao insert update da Tabela1 e passa o valor do da celula  
               
              // Imprime o conteudo que o usuario digitou na Celula  
              System.out.println(((JTextField)component).getText());
              System.out.println(vparcelas.getValueAt(vparcelas.getSelectedRow(), 3));
               
             
              //AlteraAssociado(((JTextField)component).getText());                
              //System.out.println(((JTextField)component).getText());                 
           
             return ((JTextField)component).getText();  
         }  
     }   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        vparcelas = new javax.swing.JTable();
        parcelas = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Buscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Venda à Prazo");

        jLabel1.setText("Total:");

        total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        total.setEnabled(false);
        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });

        jLabel2.setText("Cliente:");

        cliente.setEnabled(false);

        vparcelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null,null}

            },
            new String [] {
                "...", "Descricao", "Vencimento","Valor"
            }
        ));
        vparcelas.setFocusable(false);
        jScrollPane1.setViewportView(vparcelas);

        parcelas.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));
        parcelas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                parcelasStateChanged(evt);
            }
        });
        parcelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                parcelasKeyPressed(evt);
            }
        });

        jLabel3.setText("Qtde de Parcelas:");

        jButton1.setText("Finalizar a Venda");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Buscar.setText("Buscar");
        Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(parcelas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(Buscar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Buscar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void parcelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_parcelasKeyPressed

    }//GEN-LAST:event_parcelasKeyPressed

    public boolean getSucesso(){
        return sucesso;
    }
    
    private void parcelasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_parcelasStateChanged
        
        DefaultTableModel model=((DefaultTableModel)vparcelas.getModel());
        model.setNumRows(0);
       // if (parcelas.getValue().equals("1")){
          //  model.isCellEditable(0, 0);
       // }
        
        Date minhaData = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minhaData);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");


        String aux = total.getText().replace(".", "");
        Double valor = Double.parseDouble(aux.replace(",", "."))/ Double.parseDouble(parcelas.getValue().toString());
        int letra = 64;
        for (int i = 0;i<Integer.parseInt(parcelas.getValue().toString()); i++){

            letra ++;
            calendar.setTime(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 30);

            model.addRow(new Object [] {(char)letra,"Venda a prazo", dataFormatada.format(calendar.getTime()) ,df.format(valor)} );

        }
        
        System.out.println("prazo "+total.getText());
        //String aux = total.getText().replace(".", "");
        float totalcompra = Float.parseFloat(aux.replace(",", "."));
        
        j = new JTextField();
        j.setHorizontalAlignment(j.CENTER);
        j.setDocument(new NumeroDocumentJtabel(15,vparcelas,totalcompra));
        
        
        vparcelas.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(j));
        
    }//GEN-LAST:event_parcelasStateChanged

    private void BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarActionPerformed
        buscar_cliente b = new buscar_cliente(null, rootPaneCheckingEnabled);
        b.setVisible(true);// TODO add your handling code here:
        nome = b.getNome();
        cpf = b.getCpf();
        codigo = b.getCodigo();
        
        cliente.setText(nome);
    }//GEN-LAST:event_BuscarActionPerformed

    public void setTotal(String total){
        this.total.setText(total);


        ((DefaultEditor) parcelas.getEditor()).getTextField().setEditable(false);
        DefaultTableModel model=((DefaultTableModel)vparcelas.getModel());
        model.setNumRows(0);
        
        Date minhaData = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minhaData);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");


        model.addRow(new Object [] {"A","Venda a prazo",dataFormatada.format(calendar.getTime()),this.total.getText()});
    }
    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (cliente.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Selecione um cliente.");
            Buscar.requestFocus();
            return;
        }
        sucesso = true;
        dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sucesso = false;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public JTable getCupom(){
        return vparcelas;
    }

    public String getCodigo(){
        return codigo;
    }

    public String getNome(){
        return nome;
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                framePrazo dialog = new framePrazo(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Buscar;
    private javax.swing.JTextField cliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner parcelas;
    private javax.swing.JTextField total;
    private javax.swing.JTable vparcelas;
    // End of variables declaration//GEN-END:variables

}
