/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * buscar_cliente.java
 *
 * Created on 13/08/2011, 09:32:02
 */

package pdvjonatas;

import utilitarios.bd;
import utilitarios.mySql;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import utilitarios.ImprimiCabecalho;
import utilitarios.MascaraMonetaria;
import utilitarios.monetario;
/**
 *
 * @author AGENCIA GLOBO
 */
public class frameBuscarComanda extends javax.swing.JDialog {

    public String nome, cpf, codigo;
    private DecimalFormat df ;
    public Float total = new Float(0);
    public Float totalCanc = new Float(0);
    private boolean addPrincipal = false;
    
    /** Creates new form buscar_cliente */
    public frameBuscarComanda(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        busca.requestFocus();
        completa();
        df = new DecimalFormat ("#,##0.00"); 
        
        busca.setDocument( new PlainDocument(){
            @Override
		protected void insertUpdate( DefaultDocumentEvent chng, AttributeSet attr ){

                        super.insertUpdate( chng, attr );
                        completa();
		}
        });
        
       /* clientes.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(j));
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        clientes.getColumnModel().getColumn(3).setCellRenderer(centralizado);*/


        clientes.getColumnModel().getColumn(0).setMinWidth(40);
        clientes.getColumnModel().getColumn(0).setPreferredWidth(40);
        clientes.getColumnModel().getColumn(0).setMaxWidth(40);

        clientes.getColumnModel().getColumn(1).setMinWidth(200);
        clientes.getColumnModel().getColumn(1).setPreferredWidth(200);
        clientes.getColumnModel().getColumn(1).setMaxWidth(200);

        clientes.getColumnModel().getColumn(2).setMinWidth(70);
        clientes.getColumnModel().getColumn(2).setPreferredWidth(70);
        clientes.getColumnModel().getColumn(2).setMaxWidth(70);
        
        clientes.getColumnModel().getColumn(3).setMinWidth(60);
        clientes.getColumnModel().getColumn(3).setPreferredWidth(60);
        clientes.getColumnModel().getColumn(3).setMaxWidth(60);
    
    }
    
    public void completa(){
     
            // = b.fieldbyname("VALOR_TOT_NOTA").replace(".", "");
           // total = MascaraMonetaria.mascaraDinheiro(Double.parseDouble(total.replace(",", ".")),Monetario.DINHEIRO_REAL);

           // model.addRow(new Object[]{getUltimoID(Integer.parseInt(ordem)), forn, b.fieldbyname("NOTA"), total});
                        DefaultTableModel model = ((DefaultTableModel)clientes.getModel());
                        model.setNumRows(0);
                        DecimalFormat dc = new DecimalFormat ("#,##0.00");  
                        //String s = df.format (12345.67);  
//double d = df.parse (s).doubleValue(); 
                        bd b = new bd();
                        b.connect();
                        mySql f = new mySql(b.conn);
                        String sql = "Select * FROM CMD_CAB WHERE CLI_NOME LIKE '%"+busca.getText().toLowerCase()+"%' OR CLI_NOME LIKE '%"+busca.getText().toUpperCase()+"%' OR NUMERO = '"+busca.getText() +"' ORDER BY CLI_NOME ASC";
                        f.open(sql);
                        while (f.next()){
                             String total = f.fieldbyname("valor_total").replace(".", "");
                             String totalp = f.fieldbyname("valor_prod").replace(",", ".");
                             
                             model.addRow(new Object [] {
                                 f.fieldbyname("NUMERO"), 
                                 f.fieldbyname("CLI_NOME"),
                                 invertData(f.fieldbyname("DATA_ABER")),
                                 f.fieldbyname("HORA_ABER"),
                                 dc.format(Double.parseDouble(f.fieldbyname("valor_prod"))),
                                 dc.format(Double.parseDouble(f.fieldbyname("valor_desc"))),
                                 "",//dc.format(Double.parseDouble(f.fieldbyname("ACRESCIMO"))),
                                 dc.format(Double.parseDouble(f.fieldbyname("VALOR_COMISSAO"))),
                                 dc.format(Double.parseDouble(f.fieldbyname("valor_total")))
                             } );
                        }
    }

    public String invertData(String d) {
        String data[] = d.split("-");
        return data[2] + "/" + data[1] + "/" + data[0];
    }

    private Float conversor(String texto){
        String v = texto.replace(".", "");
        return Float.parseFloat(v.replace(",", "."));
    }
    public String getNome(){
        return nome;
    }

    public String getCpf(){
        return cpf;
    }

    public String getCodigo(){
        return codigo;
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
        busca = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        clientes = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        imprimiConferencia = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Conta ou nome:");

        clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Conta", "Nome", "Data", "Hora", "Produtos", "Desc.", "Acrésc.", "Comissão", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(clientes);

        jButton1.setText("Selecionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        imprimiConferencia.setText("Imprimir Conferência");
        imprimiConferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimiConferenciaActionPerformed(evt);
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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(busca, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imprimiConferencia))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(imprimiConferencia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientesMouseClicked
        if (evt.getClickCount() == 2){
            DefaultTableModel model = ((DefaultTableModel)clientes.getModel());
            nome = model.getValueAt(clientes.getSelectedRow(), 1).toString();
            cpf  = model.getValueAt(clientes.getSelectedRow(), 2).toString();
            codigo = model.getValueAt(clientes.getSelectedRow(), 0).toString();
            addPrincipal = true;
            dispose();
        }
    }//GEN-LAST:event_clientesMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            DefaultTableModel model = ((DefaultTableModel)clientes.getModel());
            nome = model.getValueAt(clientes.getSelectedRow(), 1).toString();
            cpf  = model.getValueAt(clientes.getSelectedRow(), 2).toString();
            codigo = model.getValueAt(clientes.getSelectedRow(), 0).toString();
            addPrincipal = true;
            dispose();
        }catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void imprimiConferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimiConferenciaActionPerformed
        total = new Float(0);
        totalCanc = new Float(0);
        try{
            DefaultTableModel model = ((DefaultTableModel)clientes.getModel());
            nome = model.getValueAt(clientes.getSelectedRow(), 1).toString();
            cpf  = model.getValueAt(clientes.getSelectedRow(), 2).toString();
            Main.comanda_mesa = codigo = model.getValueAt(clientes.getSelectedRow(), 0).toString();
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        BufferedWriter bw = null;
        File printer = null;
        
        try {

           printer = new File(Main.printering);
           bw  = new BufferedWriter(new FileWriter(printer));


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            String sql = "";
            
            String comissao = "";
            f.open("SELECT GAR_PER_CAIXA FROM CMD_CONFIG ");
            while (f.next()){
                comissao = f.fieldbyname("GAR_PER_CAIXA");
            }
            
            String aux = comissao.replace(",", ".");
            

            new ImprimiCabecalho().head(bw);

            bw.write("------------------------------------------\n");
            bw.write("           Gerencial - Conferencia        \n");
            bw.write("Mesa: "+Main.comanda_mesa+"               \n");
            bw.write("------------------------------------------\n");
            
            
            f.open("SELECT * FROM CMD_ITENS WHERE NUMERO = '"+codigo+"'");
            while (f.next()){
                String qtd = f.fieldbyname("qtd");
                String vuni = f.fieldbyname("valor_unit");
                String cancelado = f.fieldbyname("cancelado");
                Float mult = multiplica(qtd,vuni);
                
                
                if (cancelado.equals("1")){//objeto cancelado nao deve ser somado
                    bw.write("Canc. "+f.fieldbyname("codigo") + "   "+f.fieldbyname("descricao") + "\n");
                    bw.write("Canc. "+"     "+df.format(Double.parseDouble(qtd))+" x "+df.format(Double.parseDouble(vuni))+"    "+df.format(mult) + "\n");
                    totalCanc += mult;
                }else{
                    bw.write(f.fieldbyname("codigo") + "   "+f.fieldbyname("descricao") + "\n");
                    bw.write("     "+df.format(Double.parseDouble(qtd))+" x "+df.format(Double.parseDouble(vuni))+"    "+df.format(mult) + "\n");
                    System.out.println(mult+" teste");
                    total += mult;
                }
                
                

            }
            bw.write("\n__________________________________________\n");
           
            f.open("SELECT * from CMD_CAB WHERE numero ='"+Main.comanda_mesa+"'");
            while (f.next()){
                bw.write("\n\nComissao \tR$ "+ df.format(Double.parseDouble(f.fieldbyname("VALOR_COMISSAO"))) + "\n");   
                bw.write("Descontos \tR$ "+ df.format(Double.parseDouble(f.fieldbyname("VALOR_DESC"))) +"\n");
            }
            f.open("SELECT SUM(valor_acre) as total FROM CMD_ITENS WHERE numero = '"+Main.comanda_mesa+"'");
            while (f.next()){
                bw.write("Acresc. \tR$ "+ df.format(Double.parseDouble(f.fieldbyname("total"))) +"\n");
            }
            bw.write("Cancelado \tR$ "+ df.format(totalCanc) +"\n");
            bw.write("TOTAL \tR$ " + df.format(total+(total*(Float.parseFloat(aux)/100))) + "\n");

            
            
            
            for (int i=0;i<=Main.controlepapel;i++){
                bw.write("\n");
            }
            
            if(Main.cortarpapel)
                bw.write((char)27+(char)109);
            
            addPrincipal = false;
            Main.comanda_mesa = "";
            
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (bw != null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(frameBuscarComanda.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            addPrincipal = false;
            Main.comanda_mesa = "";
        }
    }//GEN-LAST:event_imprimiConferenciaActionPerformed

    public boolean getAddPrincipal(){
        return addPrincipal;
    }
    private Float multiplica(String qtd, String vuni){
        Float q = Float.parseFloat(qtd);
        Float u = Float.parseFloat(vuni);
        Float total = q * u;
        System.out.println(total);
        return total;
    }
    
   /* private Float totalizador(){
        Float totalTotalizador = conversor(total);
        totalTotalizador += ptotal;
        return totalTotalizador;
    }*/
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameBuscarComanda dialog = new frameBuscarComanda(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField busca;
    private javax.swing.JTable clientes;
    private javax.swing.JButton imprimiConferencia;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
