/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdvjonatas;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import utilitarios.NumeroDocument;

/**
 *
 * @author Jonatas
 */
public class frameAddtaxas extends javax.swing.JDialog {

    /**
     * Creates new form frameAddtaxas
     */
    
    private boolean taxas = false;
    private float total = 0;
    private DefaultListModel model;
    public frameAddtaxas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        valor.setDocument(new NumeroDocument(12));
        model = new DefaultListModel();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        taxa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        valor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        totalizador = new javax.swing.JList();
        Finalizar = new javax.swing.JButton();
        voltar = new javax.swing.JButton();
        add = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setText("Tipo:");

        taxa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                taxaKeyPressed(evt);
            }
        });

        jLabel2.setText("Valor:");

        valor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                valorKeyPressed(evt);
            }
        });

        totalizador.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        totalizador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalizadorKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(totalizador);

        Finalizar.setText("Finalizar");
        Finalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinalizarActionPerformed(evt);
            }
        });

        voltar.setText("Voltar");
        voltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltarActionPerformed(evt);
            }
        });

        add.setText("Ok");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taxa, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(add)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(voltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Finalizar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Finalizar)
                    .addComponent(voltar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void limpa(){
        taxa.setText("");
        valor.setText("0,00");
        taxa.requestFocus();
    }
    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
         if (taxa.getText().equals("") && valor.getText().equals("0,00")){
             if (taxa.getText().equals(""))
                 taxa.requestFocus();
             else
                 valor.requestFocus();
             return;// TODO add your handling code here:
         }
         model.addElement(taxa.getText()+"\t R$ "+valor.getText());
         totalizador.setModel(model);
         total += conversor(valor.getText());
         limpa();
    }//GEN-LAST:event_addActionPerformed

    public boolean getVoltar(){
        return taxas;
    }
    
    public float getTotalTaxas(){
        return total;
    }
    
    public DefaultListModel getModel(){
        return model;
    }
    
    public void setModel(DefaultListModel m){
        model = m;
        totalizador.setModel(model);
    }
    
    private Float conversor(String texto){
        String v = texto.replace(".", "");
        return Float.parseFloat(v.replace(",", "."));
    }
    
    private void FinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FinalizarActionPerformed
        System.out.println(totalizador.getModel().getSize());
        if (totalizador.getModel().getSize() > 0){
            taxas = true;
            dispose();
        }else{
            taxas = false;
            dispose();
        }
    }//GEN-LAST:event_FinalizarActionPerformed

    private void voltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltarActionPerformed
        taxas = false;
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_voltarActionPerformed

    private void totalizadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalizadorKeyPressed
            if (evt.getKeyCode() == 127){
                //String valor = totalizador.getSelectedValue().split("-");
                
                String []valortaxa = model.getElementAt(totalizador.getSelectedIndex()).toString().split(" ");
                total -= conversor(valortaxa[valortaxa.length-1]);
                model.removeElementAt(totalizador.getSelectedIndex());
                System.out.println(total);
                taxa.requestFocus();
            }
    }//GEN-LAST:event_totalizadorKeyPressed

    private void taxaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxaKeyPressed
        if (evt.getKeyCode() != 10)
            return;
        valor.requestFocus();
    }//GEN-LAST:event_taxaKeyPressed

    private void valorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorKeyPressed
        if (evt.getKeyCode() != 10){
            return;
        }
            if (taxa.getText().equals("") || valor.getText().equals("0,00")){
                 if (taxa.getText().equals(""))
                     taxa.requestFocus();
                 else
                     valor.requestFocus();
                 return;// TODO add your handling code here:
             }
             model.addElement(taxa.getText()+"\t R$ "+valor.getText());
             totalizador.setModel(model);
             total += conversor(valor.getText());
             limpa();
        
    }//GEN-LAST:event_valorKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frameAddtaxas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameAddtaxas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameAddtaxas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameAddtaxas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameAddtaxas dialog = new frameAddtaxas(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Finalizar;
    private javax.swing.JButton add;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField taxa;
    private javax.swing.JList totalizador;
    private javax.swing.JTextField valor;
    private javax.swing.JButton voltar;
    // End of variables declaration//GEN-END:variables
}
