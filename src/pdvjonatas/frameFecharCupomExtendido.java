/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdvjonatas;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import utilitarios.ImprimiCabecalho;
import utilitarios.NumeroDocument;
import utilitarios.bd;
import utilitarios.mySql;
import utilitarios.nivelUsuario;

/**
 *
 * @author Jonatas
 */
public class frameFecharCupomExtendido extends javax.swing.JDialog {

    /**
     * Creates new form frameFecharCupomExtendido
     */
    
    private DecimalFormat df;
    private float total;
    private JList produtos;
    private String total_produtos;
    private JTable cupom;
    private String codigo, nome;
    private boolean voltar = false;
    DecimalFormat dc = new DecimalFormat ("#,##0.00"); 
    private double desconto = 0;
    
    private String enome, ebairro, eendereco, etelefone, eobservacoes, euf, ecidade, ecelular, ecodigo = "";
    private boolean eentrega = false;
    
    private DefaultListModel model;
    
    private float totalRecebido = 0;
    private float totaltroco = 0;
    
    private boolean vPrazo = false;
    
    private DefaultListModel taxasModel;
    private boolean modelIniciadoTaxas = false;
    private float totaltaxas = 0;
    
    public frameFecharCupomExtendido(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Finalizar venda");
        lbltaxasadicionais.setVisible(false);
        
        
        df = new DecimalFormat("###,##0.00");
        recibo.setDocument(new NumeroDocument(12));
        totalizador.removeAll();
        model = new DefaultListModel();
        totalizador.setModel(model);
        
        setEspecie();
        setClienteConsumidor("Consumidor");
        setEspecieDinheiro();
        
        esc.getActionMap().put("cancelar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                escActionPerformed(e);
            }
        });
        
        f10.getActionMap().put("prazo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                vendaPrazo();
            }
        });
        f10.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "prazo");
        
        esc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelar");
        
        f11.getActionMap().put("entrega", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                entrega();
            }
        });
        f11.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "entrega");
    }

    public void setEspecie(){
        especie.removeAll();
        DefaultListModel list = new DefaultListModel();
        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        f.open("SELECT * FROM ESPECIE ORDER BY numero");
        while(f.next()){
            list.addElement(f.fieldbyname("ESPECIE"));
        }
        especie.setModel(list);
    }
    
     public boolean getVoltar(){
        return voltar;
    }
       
    public void setDados(float total, JList prod) {
        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        String sql = "";
            
        String comissao = "";
        f.open("SELECT GAR_PER_CAIXA FROM CMD_CONFIG ");
        while (f.next()){
             comissao = f.fieldbyname("GAR_PER_CAIXA");
        }
        
        //System.out.println("Teste 13"+comissao);
        
        String aux = comissao.replace(",", ".");
        
        System.out.println(total+(total*(Float.parseFloat(aux)/100))+"");
        
        this.total = total;
        this.total_produtos = df.format(total);
        this.produtos = prod;
        jtotal.setText(df.format(total));
        if (Main.comanda){
            totalfinal.setText(df.format(total+(total*(Float.parseFloat(aux)/100))));
        }else{
            totalfinal.setText(df.format(total));
        }
        
        if (Main.comanda){
            recibo.setText(df.format(total+(total*(Float.parseFloat(aux)/100))));
        }else{
            recibo.setText(jtotal.getText());
        }
        
        recibo.selectAll();
        recibo.requestFocus();

    }
    
    public void setClienteConsumidor(String c){
        cliente.setText(c);
        /*bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        f.open("SELECT * FROM CLIENTE ORDER BY ESPECIE");
        while(f.next()){
            cliente.setText(f.fieldbyname("Nome"));
        }*/
        
    }
    
    public void setEspecieDinheiro(){
        especie.setSelectedIndex(0);
        recibo.requestFocus();
    }
    
    public void setTroco(){
        totaltroco = totalRecebido - conversor(totalfinal.getText());
        if (totaltroco >= 0)
            f3.setEnabled(true);
        else
            f3.setEnabled(false);
        troco.setText(df.format(totaltroco));
    }
    
    public void setRecebido(){
        float total = conversor(recibo.getText());
        totalRecebido += total;
        recebido.setText(df.format(totalRecebido));
        setTroco();
    }
    
    
    private void vendaPrazo() {
        framePrazo f = new framePrazo(null, rootPaneCheckingEnabled);
        f.setTotal(totalfinal.getText());
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        cupom = f.getCupom();
        codigo = f.getCodigo();
        nome = f.getNome();
        
        setClienteConsumidor(nome);
       // recibo.setText(jtotal.getText());
        vPrazo = f.getSucesso();
        if (vPrazo){
            home.setEnabled(false);
            f8.setEnabled(false);
            especie.setSelectedIndex(1);
        }
        recibo.requestFocus();

    }
    
    private Float conversor(String texto){
        String v = texto.replace(".", "");
        return Float.parseFloat(v.replace(",", "."));
    }
    
    public void entrega(){
        frameEntrega f = new frameEntrega(null, rootPaneCheckingEnabled);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        ecodigo = f.getEcodigo();
        enome = f.getNome().getText();
        eendereco = f.getEndereco().getText();
        etelefone = f.getTelefone().getText();
        ebairro = f.getBairro().getText();
        ecidade = f.getCidade().getText();
        eobservacoes = f.getObservacoes().getText();
        ecelular = f.getCelular().getText();
        euf = f.getUf().getSelectedItem().toString();
        eentrega = f.getValida();
        setClienteConsumidor(enome);
        recibo.selectAll();
        recibo.requestFocus();
        
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
        jtotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        totalfinal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        recibo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        f10 = new javax.swing.JButton();
        f11 = new javax.swing.JButton();
        esc = new javax.swing.JButton();
        home = new javax.swing.JButton();
        f8 = new javax.swing.JButton();
        f3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        recebido = new javax.swing.JTextField();
        troco = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obscupom = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        totalizador = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        especie = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        lbltaxasadicionais = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setText("Total:");

        jtotal.setEditable(false);
        jtotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setText("Final:");

        totalfinal.setEditable(false);
        totalfinal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalfinal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("Pagamento em:");

        recibo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        recibo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                reciboKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ações"));

        f10.setText("F10 - Prazo");
        f10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f10ActionPerformed(evt);
            }
        });

        f11.setText("F11 - Entrega");
        f11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f11ActionPerformed(evt);
            }
        });

        esc.setText("Esc - Cancelar");
        esc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                escActionPerformed(evt);
            }
        });

        home.setText("Home - Alterar Total");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        f8.setText("F8 - Adicionar Taxas");
        f8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f8ActionPerformed(evt);
            }
        });

        f3.setText("F3 - Finalizar");
        f3.setEnabled(false);
        f3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f3ActionPerformed(evt);
            }
        });
        f3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                f3KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(f10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(f11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(esc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(f8, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
            .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(f3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(f10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(home)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(esc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f3))
        );

        jLabel4.setText("Cliente:");

        cliente.setEditable(false);

        recebido.setEditable(false);
        recebido.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        recebido.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        troco.setEditable(false);
        troco.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setText("Troco:");

        jLabel6.setText("Recebido:");

        jLabel7.setText("Totalizador:");

        jLabel8.setText("Observações para o cupom:");

        obscupom.setColumns(20);
        obscupom.setRows(5);
        jScrollPane3.setViewportView(obscupom);

        totalizador.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        totalizador.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(totalizador);

        especie.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        especie.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        especie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                especieMouseClicked(evt);
            }
        });
        especie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                especieKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(especie);

        jLabel9.setText("R$ ");

        lbltaxasadicionais.setForeground(new java.awt.Color(255, 51, 51));
        lbltaxasadicionais.setText("Taxas Adicionais");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(recebido)
                                    .addGap(18, 18, 18)
                                    .addComponent(troco, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addGap(68, 68, 68)
                                            .addComponent(jLabel7))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(95, 95, 95)
                                            .addComponent(jLabel5)))
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(recibo))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel1)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbltaxasadicionais))
                                    .addComponent(totalfinal, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(lbltaxasadicionais))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalfinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recebido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(troco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        
            if (!Main.usercupomDescAcres){
                nivelUsuario n = new nivelUsuario();
                if (!n.checkAdmin("Cupom - Desconto/Acréscimo"))
                    return;
            }
            
            
            frameDesconto f = new frameDesconto(null, true);
            f.setDados(totalfinal.getText(), totalfinal.getText());
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            
            if (f.getPoronde()){
                String aux = this.totalfinal.getText().replace(".", "");
                double totalini = Double.parseDouble(aux.replace(",", "."));
                aux = f.getTotal().replace(".", "");
                double totalfinal = Double.parseDouble(aux.replace(",", "."));
                //aux = s.toString().replace(".", "");
                //double alt = Double.parseDouble(aux.replace(",", "."));
                desconto = totalini - totalfinal;
                this.totalfinal.setText(f.getTotal());
                recibo.setText(f.getTotal());
                recibo.requestFocus();
                recibo.selectAll();
            }
        
    }//GEN-LAST:event_homeActionPerformed

    private void escActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_escActionPerformed
        voltar = true;
        dispose();
    }//GEN-LAST:event_escActionPerformed

    private void f10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f10ActionPerformed
        vendaPrazo();
    }//GEN-LAST:event_f10ActionPerformed

    private void f11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f11ActionPerformed
           entrega();        // TODO add your handling code here:
    }//GEN-LAST:event_f11ActionPerformed

    private void reciboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reciboKeyPressed
        if(evt.getKeyCode() == 10 && !recibo.getText().equals("") && !recibo.getText().equals("0,00")){
            setRecebido();
            model.addElement(especie.getSelectedValue()+" \t R$ "+recibo.getText());
            //model.addElement();
            totalizador.setModel(model);
            if (totaltroco >= 0){
                f3.requestFocus();
                recibo.setText("0,00");
            }else{
                recibo.requestFocus();
                recibo.selectAll();
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_reciboKeyPressed
    
    private void f8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f8ActionPerformed
        lbltaxasadicionais.setVisible(false);
        frameAddtaxas f = new frameAddtaxas(null, true);
        if (modelIniciadoTaxas)
            f.setModel(taxasModel);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        

            totaltaxas = f.getTotalTaxas();
            totalfinal.setText(df.format(conversor(jtotal.getText())+totaltaxas));
            lbltaxasadicionais.setVisible(f.getVoltar());
            
            recibo.setText(totalfinal.getText());
            recibo.requestFocus();
            taxasModel = f.getModel();
            modelIniciadoTaxas = true;
            
        
    }//GEN-LAST:event_f8ActionPerformed

    private void f3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_f3KeyPressed
        if(evt.getKeyCode() == 10)
         finalizarVenda(vPrazo); 
    }//GEN-LAST:event_f3KeyPressed

    private void f3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f3ActionPerformed
         finalizarVenda(vPrazo); 
    }//GEN-LAST:event_f3ActionPerformed

    private void especieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_especieKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_especieKeyPressed

    private void especieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_especieMouseClicked
        recibo.selectAll();
        recibo.requestFocus();
    }//GEN-LAST:event_especieMouseClicked

    
    private void finalizarVenda(boolean prazo) {
        String modelo = Main.modelo;
        String serie  = Main.serie;
        if (recibo.getText().equals("")) {
            recibo.requestFocus();
            return;
        }
        if (!Main.comanda_mesa.equals("")){
            modelo = Main.modelo;
            serie  = Main.serie;
        }
        try {

            File printer = new File(Main.printering);
            BufferedWriter bw = new BufferedWriter(new FileWriter(printer));


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            String sql = "";
            
            String comissao = "";
            f.open("SELECT GAR_PER_CAIXA FROM CMD_CONFIG ");
            while (f.next()){
                comissao = f.fieldbyname("GAR_PER_CAIXA");
            }
            

            new ImprimiCabecalho().head(bw);

            bw.write("------------------------------------------\n");
            bw.write("              CUPOM NAO FISCAL             \n");
            bw.write("------------------------------------------\n");


            // produtos

            DefaultListModel model = new DefaultListModel();
            model = (DefaultListModel) produtos.getModel();
            
            //utilizado para o fechamento novo
            DefaultListModel totalizadorModel = new DefaultListModel();
            totalizadorModel = (DefaultListModel) totalizador.getModel();
            //String []especieVenda = totalizadorModel.getElementAt(0).toString().split(" R$ ");

            for (int i = 0; i < model.getSize(); i++) {
                bw.write(model.getElementAt(i).toString() + "\n");
            }
            if (!Main.comanda_mesa.equals("")){
                f.open("SELECT * from CMD_CAB where numero ='"+Main.comanda_mesa+"'");
                while (f.next()){
                    bw.write("\n\nComissao \t R$ "+ df.format(Double.parseDouble(f.fieldbyname("VALOR_COMISSAO"))) + "\n");   
                    bw.write("Descontos \t R$ "+ df.format(Double.parseDouble(f.fieldbyname("VALOR_DESC"))) +"\n");
                }
                f.open("SELECT SUM(valor_acre) as total from CMD_ITENS where numero = '"+Main.comanda_mesa+"'");
                while (f.next()){
                    bw.write("Acrescimos \t R$ "+ df.format(Double.parseDouble(f.fieldbyname("total"))) +"\n");
                }
            }

            bw.write("\n__________________________________________\n");
            
            bw.write("TOTAL \t R$ " + totalfinal.getText() + "\n");
            for (int i=0; i<totalizadorModel.getSize();i++){
                bw.write(totalizadorModel.getElementAt(i).toString() + "\n");
            }
            if (totaltaxas > 0)
            for (int i=0; i<taxasModel.getSize();i++){
                bw.write(taxasModel.getElementAt(i).toString() + "\n");
            }
            bw.write("RECEBIDO \t R$ " + recebido.getText() + "\n");
            bw.write("DESCONTO \t R$ " + dc.format(this.desconto) + "\n");
            bw.write("TROCO \t R$ " + troco.getText() + "\n\n\n\n\n");
           
            if (eentrega){
                bw.write("              DADOS PARA ENTREGA            \n");
                bw.write("Nome:  " + enome + "\n");
                bw.write("Endereco: " + eendereco + "\n");
                bw.write("Telefone: " + etelefone +" Celular: " +ecelular +"\n");
                bw.write("Cidade: " + ecidade + " UF: "+ euf + "\n");
                bw.write("Obs.: " + eobservacoes +"\n\n");
            }
            eentrega = false;
            
            if (!obscupom.getText().equals(""))
                bw.write("Obs.: " + obscupom.getText() +"\n\n\n");
            
            bw.write(Main.mensagem_empresa);
            //bw.write("\n\n\n\n\n\n\n");
            for (int i=0;i<=Main.controlepapel;i++){
                bw.write("\n");
            }
            if(Main.cortarpapel)
                bw.write((char)27+(char)109);
            bw.flush();
            bw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int ult = 0;
        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);


        try {


            DefaultListModel model = new DefaultListModel();
            model = (DefaultListModel) produtos.getModel();

            f.open("SELECT MAX(NOTA) as NOTA FROM VENDAS WHERE MODELO ='"+modelo+"' AND serie ='"+serie+"'");//pega o id para proxima venda
            while (f.next()) {
                try{
                    ult = Integer.parseInt(f.fieldbyname("NOTA")) + 1;
                }catch (Exception e){
                    ult=1;
                }
                /*if (f.fieldbyname("NOTA").equals("null") || f.fieldbyname("NOTA").equals("")){
                    ult = 1;
                }else*/
                    
            }
            String sCodigo = getUltimoID(ult);

            DefaultListModel totalizadorModel = new DefaultListModel();
            totalizadorModel = (DefaultListModel) totalizador.getModel();
            String []especieVenda = totalizadorModel.getElementAt(0).toString().split("\t");
            //utilizar para inserir nas observacoes da venda
            String observacoes = "Especie recebida:\n";
            for (int i=0; i<totalizadorModel.getSize();i++){
                observacoes += totalizadorModel.getElementAt(i).toString()+"\n";
            }
            observacoes +="\n Taxas Adicionais\n";
            if (totaltaxas > 0)
                for (int i=0; i<taxasModel.getSize();i++){
                    observacoes += taxasModel.getElementAt(i).toString()+" \n";
                }
            

            // utilizar mais tarde
            //System.out.println(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
            String hj = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
            String hora = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            String total_nota = total + "";
            String desconto = conversor(total_produtos) - (conversor(totalfinal.getText()) - totaltaxas) + "";
            System.out.println();
            if (ecodigo.equals(""))
                ecodigo = "000000";
            
            String sql = "INSERT INTO VENDAS (nota, modelo, serie, data_emissao, loja,caixa,saida,natureza,cfop,cliente,data_saida,hora_saida, valor_tot_pro, valor_tot_nota,cancelada,observacoes,vendedor,especie,outras_despesas ) VALUES"
                    + "('" + sCodigo + "','"+ modelo +"','"+ serie +"','" + new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()) + "'"
                    + ",'01','01','X','Venda a vista','5.102','"+ ecodigo +"',"
                    + "'" + new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()) + "','" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "',"
                    + "'" + conversor(total_produtos) + "','" + conversor(totalfinal.getText()) + "','0','" + observacoes + "',"
                    + "'"+Main.vendedor+"','"+ especieVenda[0]+"','"+totaltaxas+"'"
                    + ")";

            //===============================iasdjf asijdf iasjdf 

            System.out.println(sql);
            f.execute(sql);



            //System.out.print(model.getSize());
            int item = 1;
            double descporitem = this.desconto / model.getSize();
            for (int i = 0; i < model.getSize(); i += 2) {
                String codigo[] = model.getElementAt(i).toString().split(" ");
                String qtde[] = model.getElementAt(i + 1).toString().split(" ");
                /*for (int x=0;x<qtde.length;x++){
                 System.out.println(x +" "+qtde[x]);
                 }*/
                f.open("SELECT preco_custo, descricao, barras FROM ESTOQUE WHERE codigo = '" + codigo[0] + "'");
                f.next();
                
                String barras = f.fieldbyname("barras");
                String descricao = f.fieldbyname("descricao");
                String preco_custo = f.fieldbyname("preco_custo");

                
                String desc = "";
                if (Main.comanda_mesa.equals("")){
                    f.open("SELECT (PRECO_VENDA-"+conversor(qtde[7])+") as auxdesconto FROM ESTOQUE WHERE CODIGO = '"+codigo[0]+"'");
                    f.next();
                    desc = f.fieldbyname("auxdesconto");
                    System.out.println(conversor(qtde[7])+" asd f "+desc);
                }else{
                    f.open("SELECT VALOR_DESC FROM CMD_ITENS WHERE NUMERO = '"+Main.comanda_mesa+"'");
                    f.next();
                    desc = f.fieldbyname("VALOR_DESC");
                }
                
                
                
                sql = "INSERT INTO ITEVENDAS (NOTA,MODELO,SERIE,DATA_EMISSAO,LOJA,CAIXA,ITEM,CODIGO,BARRAS,DESCRICAO,CFOP,QTD,VALOR_UNITA,VALOR_TOTAL,VALOR_CUSTO,VALOR_TOT_PRO, CANCELADA, LIVRE, DATA_SAIDA, HORA_SAIDA,VALOR_DESCO ) VALUES"
                        + "('" + sCodigo + "','"+modelo+"','"+serie+"','" + hj + "','01','01','" + (item++) + "','" + codigo[0] + "','" + barras + "','" + descricao + "','5.102','" + conversor(qtde[5]) + "','" + conversor(qtde[7]) + "','" + conversor(qtde[11]) + "','" + preco_custo + "','" + (conversor(qtde[11])-descporitem) + "','0','1','" + hj + "','" + hora + "','"+descporitem+"' )";
                
                f.execute(sql);
                //System.out.println(sql);
                f.execute("UPDATE ESTOQUE SET QTD = QTD - " + conversor(qtde[5]) + " WHERE CODIGO = '" + codigo[0] + "'");
            }

            if (Main.comanda){
                if (!Main.comanda_mesa.equals("")){
                    sql = "DELETE FROM CMD_ITENS WHERE NUMERO = '"+Main.comanda_mesa+"'";
                    f.execute(sql);
                    sql = "Delete FROM CMD_CAB WHERE NUMERO = '"+Main.comanda_mesa+"'";
                    f.execute(sql);
                }
            }

            Main.comanda_mesa = "";


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (prazo) {

            DefaultTableModel model = ((DefaultTableModel) cupom.getModel());
            System.out.println(codigo);
            for (int i = 0; i < cupom.getRowCount(); i++) {
                String sql = "INSERT INTO RECEBER (DOCUMENTO, HISTORICO, COD_CLIENTE, NOM_CLIENTE, EMISSAO, VENCIMENTO, VALOR_DUP) VALUES "
                        + "('" + (getUltimoID(ult) + model.getValueAt(i, 0).toString()) + "', "
                        + "'PARCELA \"" + model.getValueAt(i, 0).toString() + "\" da NF N" + getUltimoID(ult) + "',"
                        + "'" + codigo + "',"
                        + "'" + nome + "',"
                        + "'" + new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()) + "',"
                        + "'" + invertData(model.getValueAt(i, 2).toString()) + "',"
                        + "'" + model.getValueAt(i, 3).toString().replace(",", ".") + "')";
                f.execute(sql);
                System.out.println(sql);

            }

            try {
                File printer = new File(Main.printering);
                BufferedWriter bw = new BufferedWriter(new FileWriter(printer));

                String sql = "";
                //f.open(sql);
                
                new ImprimiCabecalho().head(bw);

                sql = "SELECT * FROM CLIENTE WHERE CODIGO ='" + this.codigo + "'";
                f.open(sql);
                while (f.next()) {
                    for (int i = 0; i < this.cupom.getRowCount(); i++) {
                        bw.write("PARCELA \"" + model.getValueAt(i, 0).toString() + " \" da NF No " + getUltimoID(ult) + "\n");
                        bw.write("Vencimento: " + model.getValueAt(i, 2).toString() + "\n");
                        bw.write("Cliente:  " + f.fieldbyname("NOME") + "\n");
                        bw.write("Endereco: " + f.fieldbyname("ENDERECO") + "\n");
                        bw.write("Valor: " + model.getValueAt(i, 3).toString() + "\n\n\n");
                        bw.write("______________________________________________\n");
                        bw.write("              Assinatura consumidor\n\n\n");
                        bw.write("Reconheco a importancia acima e pagarei nas \ndatas fixadas.\n\n");

                    }
                }
                for (int i=0;i<=Main.controlepapel;i++){
                    bw.write("\n");
                }
                if(Main.cortarpapel)
                    bw.write((char)27+(char)109);

                bw.flush();
                bw.close();

                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        dispose();
    }
    
    public String getUltimoID(int codigo) {
        String cod = "";
        if (codigo < 10) {
            cod = "00000" + codigo;
        } else if (codigo < 100) {
            cod = "0000" + codigo;
        } else if (codigo < 1000) {
            cod = "000" + codigo;
        } else if (codigo < 10000) {
            cod = "00" + codigo;
        } else {
            cod = "0" + codigo;
        }
        return cod;
    }
    
    public String invertData(String d) {
        String data[] = d.split("/");
        return data[2] + "/" + data[1] + "/" + data[0];
    }
    
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
            java.util.logging.Logger.getLogger(frameFecharCupomExtendido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameFecharCupomExtendido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameFecharCupomExtendido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameFecharCupomExtendido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameFecharCupomExtendido dialog = new frameFecharCupomExtendido(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField cliente;
    private javax.swing.JButton esc;
    private javax.swing.JList especie;
    private javax.swing.JButton f10;
    private javax.swing.JButton f11;
    private javax.swing.JButton f3;
    private javax.swing.JButton f8;
    private javax.swing.JButton home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jtotal;
    private javax.swing.JLabel lbltaxasadicionais;
    private javax.swing.JTextArea obscupom;
    private javax.swing.JTextField recebido;
    private javax.swing.JTextField recibo;
    private javax.swing.JTextField totalfinal;
    private javax.swing.JList totalizador;
    private javax.swing.JTextField troco;
    // End of variables declaration//GEN-END:variables
}
