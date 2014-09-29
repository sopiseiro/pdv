/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * fecharCupom.java
 *
 * Created on 16/06/2011, 10:09:55
 */
package pdvjonatas;

import utilitarios.bd;
import utilitarios.mySql;
import java.awt.ComponentOrientation;
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
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static pdvjonatas.Main.comanda;
import utilitarios.ImprimiCabecalho;

public class fecharCupom extends javax.swing.JDialog {

    private DecimalFormat df;
    private float total;
    private JList produtos;
    private String total_produtos;
    private JTable cupom;
    private String codigo, nome;
    private boolean voltar = false;
    DecimalFormat dc = new DecimalFormat ("#,##0.00"); 
    private double desconto = 0;
    
    private String enome, ebairro, eendereco, etelefone, eobservacoes, euf, ecidade, ecelular, ecodigo;
    private boolean eentrega = false;
    /**
     * Creates new form fecharCupom
     */
    public fecharCupom(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        df = new DecimalFormat("###,##0.00");

        // recibo.setDocument(new MonetarioDocument());

        f3.getActionMap().put("finalizar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                finalizarVenda(false);
            }
        });
        f3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "finalizar");

        f10.getActionMap().put("prazo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                vendaPrazo();
            }
        });
        f10.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "prazo");
        
        f11.getActionMap().put("entrega", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                entrega();
            }
        });
        f11.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "entrega");

        esc.getActionMap().put("cancelar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cancelarVenda();
            }
        });
        esc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelar");

        recibo.setDocument(new PlainDocument() {
            @Override
            protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
                super.insertUpdate(chng, attr);
                Float tot = conversor(recibo.getText()) - conversor(jtotal.getText());
                troco.setText(df.format(tot));
            }
        });

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
        
    }
    public boolean getVoltar(){
        return voltar;
    }

    private void vendaPrazo() {
        framePrazo f = new framePrazo(null, rootPaneCheckingEnabled);
        f.setTotal(jtotal.getText());
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        cupom = f.getCupom();
        codigo = f.getCodigo();
        nome = f.getNome();

        recibo.setText(jtotal.getText());
        finalizarVenda(true);


    }

    private void cancelarVenda() {
        voltar = true;
        dispose();
    }

    private void finalizarVenda(boolean prazo) {
        String modelo = "1";
        String serie  = "A";
        if (recibo.getText().equals("")) {
            recibo.requestFocus();
            return;
        }
        if (!Main.comanda_mesa.equals("")){
            modelo = "88";
            serie  = "888";
        }
        try {

            File printer = new File(Main.printering);
            BufferedWriter bw = new BufferedWriter(new FileWriter(printer));


            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            String sql = "";

            new ImprimiCabecalho().head(bw);

            bw.write("------------------------------------------\n");
            bw.write("              CUPOM NAO FISCAL             \n");
            bw.write("------------------------------------------\n");


            // produtos

            DefaultListModel model = new DefaultListModel();
            model = (DefaultListModel) produtos.getModel();


            for (int i = 0; i < model.getSize(); i++) {
                bw.write(model.getElementAt(i).toString() + "\n");
            }
            if (!Main.comanda_mesa.equals("")){
                f.open("SELECT * from Cabcomanda where comanda_mesa ='"+Main.comanda_mesa+"'");
                while (f.next()){
                    bw.write("\n\nComissao     R$ "+ df.format(Double.parseDouble(f.fieldbyname("comissao"))) + "\n");
                    bw.write("Acrescimos   R$ "+ df.format(Double.parseDouble(f.fieldbyname("acrescimo"))) +"\n");
                    bw.write("Descontos    R$ "+ df.format(Double.parseDouble(f.fieldbyname("desconto"))) +"\n");
                }
            }

            bw.write("\n__________________________________________\n");
            
            bw.write("TOTAL    R$ \t" + jtotal.getText() + "\n");
            bw.write("RECEBIDO R$ \t" + recibo.getText() + "\n");
            bw.write("DESCONTO R$ \t" + dc.format(this.desconto) + "\n");
            bw.write("TROCO    R$ \t" + troco.getText() + "\n\n\n\n\n");
           
            if (eentrega){
                bw.write("              DADOS PARA ENTREGA            \n");
                bw.write("Nome:  " + enome + "\n");
                bw.write("Endereco: " + eendereco + "\n");
                bw.write("Telefone: " + etelefone +" Celular: " +ecelular +"\n");
                bw.write("Cidade: " + ecidade + " UF: "+ euf + "\n");
                bw.write("Obs.: " + eobservacoes +"\n\n\n");
            }
            eentrega = false;
            
            bw.write(Main.mensagem_empresa);
            //bw.write("\n\n\n\n\n\n\n");
            bw.write("\n\n\n\n\n"+(char)27+(char)109);
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


            // utilizar mais tarde
            //System.out.println(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
            String hj = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
            String hora = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            String total_nota = total + "";
            String desconto = conversor(total_produtos) - conversor(jtotal.getText()) + "";
            System.out.println();
            if (ecodigo.equals(""))
                ecodigo = "000000";
            
            String sql = "INSERT INTO VENDAS (nota, modelo, serie, data_emissao, loja,caixa,saida,natureza,cfop,cliente,data_saida,hora_saida, valor_tot_pro, valor_tot_nota,cancelada,observacoes,vendedor ) VALUES"
                    + "('" + sCodigo + "','"+ modelo +"','"+ serie +"','" + new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()) + "'"
                    + ",'01','01','X','Venda a vista','5.102','"+ ecodigo +"',"
                    + "'" + new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()) + "','" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "',"
                    + "'" + conversor(total_produtos) + "','" + conversor(jtotal.getText()) + "','0','" + (df.format(conversor(desconto))) + "',"
                    + "'"+Main.vendedor+"'"
                    + ")";



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
                    f.open("SELECT VALOR_DESCO FROM ITECOMANDA WHERE COMANDA_MESA = '"+Main.comanda_mesa+"'");
                    f.next();
                    desc = f.fieldbyname("VALOR_DESCO");
                }
                
                
                
                sql = "INSERT INTO ITEVENDAS (NOTA,MODELO,SERIE,DATA_EMISSAO,LOJA,CAIXA,ITEM,CODIGO,BARRAS,DESCRICAO,CFOP,QTD,VALOR_UNITA,VALOR_TOTAL,VALOR_CUSTO,VALOR_TOT_PRO, CANCELADA, LIVRE, DATA_SAIDA, HORA_SAIDA,VALOR_DESCO ) VALUES"
                        + "('" + sCodigo + "','"+modelo+"','"+serie+"','" + hj + "','01','01','" + (item++) + "','" + codigo[0] + "','" + barras + "','" + descricao + "','5.102','" + conversor(qtde[5]) + "','" + conversor(qtde[7]) + "','" + conversor(qtde[11]) + "','" + preco_custo + "','" + (conversor(qtde[11])-descporitem) + "','0','1','" + hj + "','" + hora + "','"+descporitem+"' )";
                
                f.execute(sql);
                //System.out.println(sql);
                f.execute("UPDATE ESTOQUE SET QTD = QTD - " + conversor(qtde[5]) + " WHERE CODIGO = '" + codigo[0] + "'");
            }

            if (Main.comanda){
                sql = "DELETE FROM ITECOMANDA WHERE COMANDA_MESA = '"+Main.comanda_mesa+"'";
                f.execute(sql);
                sql = "Delete FROM CABCOMANDA WHERE COMANDA_MESA = '"+Main.comanda_mesa+"'";
                f.execute(sql);
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
                f.open(sql);
                
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
                        bw.write("______________________________________________\n\n");
                        bw.write("              Assinatura consumidor\n\n\n");
                        bw.write("Reconheco a importancia acima e pagarei nas \ndatas fixadas.\n\n");

                    }
                }
                bw.write("\n\n\n"+(char)27+(char)109);

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
    
    private Float conversor(String texto){
        String v = texto.replace(".", "");
        return Float.parseFloat(v.replace(",", "."));
    }

    public void setDados(float total, JList prod) {
        this.total = total;
        this.total_produtos = df.format(total);
        this.produtos = prod;
        jtotal.setText(df.format(total));
        recibo.setText(jtotal.getText());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtotal = new javax.swing.JTextField();
        troco = new javax.swing.JTextField();
        f3 = new javax.swing.JLabel();
        esc = new javax.swing.JLabel();
        f10 = new javax.swing.JLabel();
        recibo = new javax.swing.JTextField();
        f11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Cupom");
        setUndecorated(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Total:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Recebido:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Troco:");

        jtotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtotal.setEnabled(false);
        jtotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtotalMouseClicked(evt);
            }
        });

        troco.setEditable(false);
        troco.setBackground(new java.awt.Color(255, 255, 204));
        troco.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        troco.setForeground(new java.awt.Color(255, 0, 0));
        troco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        f3.setText("F3 - Finalizar");

        esc.setText(" ESC - Voltar");

        f10.setText("F10 - Prazo");

        recibo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        recibo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        recibo.setText("0,00");
        recibo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                reciboKeyPressed(evt);
            }
        });

        f11.setText("F11 - Entrega");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(f11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(f10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recibo, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(f3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(esc))
                    .addComponent(troco, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addComponent(jtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(esc)
                    .addComponent(f3)
                    .addComponent(f10)
                    .addComponent(f11))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(recibo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(troco, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reciboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reciboKeyPressed
        Float tot = conversor(troco.getText());
        if (evt.getKeyCode() == 10 && tot >= 0) {
            finalizarVenda(false);
        }
    }//GEN-LAST:event_reciboKeyPressed

    private void jtotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtotalMouseClicked
        if (evt.getClickCount() == 2){
            if (!Main.vendedor.equals("")){
                bd b = new bd();
                b.connect();
                mySql bd = new mySql(b.conn);
                bd.open("SELECT DESCITEM FROM USUARIOS WHERE USUARIO ='"+Main.vendedor+"'");
                if (bd.next()){
                    if (!bd.fieldbyname("descitem").equals("1") ){
                        JOptionPane.showMessageDialog(null, "Você não tem permissão para essa ação!");
                        return;
                    }

                }
            }
            
            frameDesconto f = new frameDesconto(null, true);
            f.setDados(jtotal.getText(), jtotal.getText());
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            
            if (f.getPoronde()){
                String aux = jtotal.getText().replace(".", "");
                double totalini = Double.parseDouble(aux.replace(",", "."));
                aux = f.getTotal().replace(".", "");
                double totalfinal = Double.parseDouble(aux.replace(",", "."));
                //aux = s.toString().replace(".", "");
                //double alt = Double.parseDouble(aux.replace(",", "."));
                desconto = totalini - totalfinal;
                jtotal.setText(f.getTotal());
                recibo.setText(f.getTotal());
            }
        }
    }//GEN-LAST:event_jtotalMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                fecharCupom dialog = new fecharCupom(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel esc;
    private javax.swing.JLabel f10;
    private javax.swing.JLabel f11;
    private javax.swing.JLabel f3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jtotal;
    private javax.swing.JTextField recibo;
    private javax.swing.JTextField troco;
    // End of variables declaration//GEN-END:variables
}
