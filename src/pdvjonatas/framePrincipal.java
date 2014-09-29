/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * framePrincipal.java
 *
 * Created on 02/06/2011, 11:43:20
 */

package pdvjonatas;

import itensMenu.cancelarUltimaVenda;
import itensMenu.frameCadastrarEspecie;
import utilitarios.bd;
import utilitarios.mySql;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import utilitarios.NumeroDocument;
import utilitarios.monetario;
import utilitarios.nivelUsuario;


/**
 *
 * @author TEMP
 */
public class framePrincipal extends javax.swing.JFrame {
    private int cod[];
    private int qtd[];
    private int indice;
    private boolean visivel = false;
    private DefaultListModel model;
    private DecimalFormat df ;

    private Float auxtotal;
    private Float auxqtde;
    private Float auxuni;

    public static final JCheckBoxMenuItem imprimir = null;
    
    NumberFormat formato1 = NumberFormat.getCurrencyInstance(); //Formato de moeda atual do sistema
    NumberFormat formato2 = NumberFormat.getInstance(); //Formato atual do sistema
    NumberFormat formato3 = NumberFormat.getNumberInstance(); //Formato numérico atual do sistema
    NumberFormat formato4 = NumberFormat.getCurrencyInstance(new Locale("es", "ES")); //Formato de moeda (Espanha)
    NumberFormat formato5 = NumberFormat.getIntegerInstance(); //Formato de inteiro atual do sistema
    NumberFormat formato6 = NumberFormat.getPercentInstance(); //Formato de porcentagem atual do sistema
   
    /** Creates new form framePrincipal */
    public framePrincipal() throws BadLocationException {
        initComponents();
        
        ImageIcon imagemTituloJanela = new ImageIcon("gdoor.png");  
        setIconImage(imagemTituloJanela.getImage());  
        
        vuni.setEditable(Main.useritemDescAcres);
        vuni.setFocusable(Main.useritemDescAcres);
        
        
        
        
        busca.requestFocus();
        model = new DefaultListModel();
        cod = new int[999];
        qtd = new int[999];
        indice = 0;
        total.setText("0.00");
        fX.setVisible(false);
        fX1.setVisible(false);
        jPanel1.setVisible(false);
        
        if (Main.vendedor.equals("")){
            labelvendedor.setVisible(false);
            jlabel18.setVisible(false);
        }else{
            labelvendedor.setText(Main.vendedor);
        }
        
        importar_tecla.setVisible(Main.comanda);
        importar.setVisible(Main.comanda);
        
        vuni.setDocument(new NumeroDocument(12));
        
        importar_tecla.getActionMap().put("importar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                importarComanda();
            }
        });
        importar_tecla.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "importar");
        


        esc.getActionMap().put("fecharprograma", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                         if (!Main.usercupomCancelar){
                            nivelUsuario n = new nivelUsuario();
                            if (!n.checkAdmin("Cupom - Cancelar"))
                                return;
                        }
                         if (JOptionPane.showConfirmDialog(null, "Deseja realmente sair?") == 0)
                                System.exit(0);
                         else
                             busca.requestFocus();

		}
	});
        esc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fecharprograma");

        f2.getActionMap().put("cancelar", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                     if (!Main.usercupomCancelar){
                        nivelUsuario n = new nivelUsuario();
                        if (!n.checkAdmin("Cupom - Cancelar"))
                            return;
                     }
                    if (JOptionPane.showConfirmDialog(null, "Cancelar o Cupom Aberto?") == 0){
                            vuni.setText("");
                            model.removeAllElements();
                            nomeprodutos.setText("Caixa Livre");
                            mult.setText("Aguardando...");
                            total.setText("0.00");
                            jScrollPane2.setVisible(false);
                            busca.setText("");
                    } else
                             busca.requestFocus();

		}
	});
        f2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "cancelar");

        f3.getActionMap().put("fecharCupom", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        if (model.getSize() > 0)
                            fecharCupom();
                        else
                            busca.requestFocus();

		}
	});
        f3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "fecharCupom");
        
        fX.getActionMap().put("leituraX", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        if (!Main.usercupomCancelar){
                            nivelUsuario n = new nivelUsuario();
                            if (!n.checkAdmin("Cupom - Cancelar"))
                                return;
                        }
                        FrameLeituraX valores = new FrameLeituraX(null, true);
                        //valores.setValor(vuni.getText());
                        valores.setLocationRelativeTo(null);
                        valores.setVisible(true);
                             
                        
                        
                    }
	});
        fX.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "leituraX");


        insert.getActionMap().put("quantidade", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        qtde.selectAll();
                        qtde.requestFocus();
		}
	});
        insert.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "quantidade");

        pageup.getActionMap().put("vuni", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        if (!Main.useritemDescAcres){
                            nivelUsuario n = new nivelUsuario();
                            if (n.checkAdmin("Item - Desconto/Acréscimo")){
                                InserirValoresDiversos valores = new InserirValoresDiversos(null, enabled);
                                valores.setValor(vuni.getText());
                                valores.setLocationRelativeTo(null);
                                valores.setVisible(true);
                                
                                if (!valores.cancelado())
                                    vuni.setText(valores.getValor());
                            }else
                                return;

                        }else{
                            vuni.requestFocus();
                            vuni.selectAll();
                        }
                        
		}
	});
        pageup.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "vuni");


        df = new DecimalFormat("###,##0.00");
        df.applyPattern("###,##0.00");
        df.getInstance(new Locale("pt", "BR"));
        Locale.setDefault(new Locale("pt", "BR"));

        jScrollPane2.setVisible(false);
        busca.setDocument( new PlainDocument(){
            @Override
		protected void insertUpdate( DefaultDocumentEvent chng, AttributeSet attr ){
                        
                        super.insertUpdate( chng, attr );
                        try{
                            Double.parseDouble(busca.getText());
                            visivel = false;
                        }catch (NumberFormatException e){
                            
                            DefaultListModel list = new DefaultListModel();
                            jScrollPane2.setVisible(true);

                            visivel = true;
                            bd b = new bd();
                            b.connect();
                            mySql f = new mySql(b.conn);
                            String sql = "Select codigo,descricao, preco_venda FROM ESTOQUE WHERE codigo LIKE '%"+busca.getText()+"%' OR barras = '"+busca.getText() +"' OR descricao LIKE '%"+busca.getText().toUpperCase()+"%' Order by descricao";
                            f.open(sql);
                            while (f.next()){
                                list.addElement(f.fieldbyname("codigo")+" - "+f.fieldbyname("descricao")+" - R$ "+df.format(Double.parseDouble(f.fieldbyname("PRECO_VENDA").replace(",", "."))));
                            }
                            pesquisa.setModel(list);
                            pesquisa.setSelectedIndex(0);

                            if (pesquisa.getSelectedIndex() < 0){
                                jScrollPane2.setVisible(false);
                                visivel = false;
                            }
                        }
		}

	});
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        produtos = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        mult = new javax.swing.JTextField();
        nomeprodutos = new javax.swing.JTextField();
        vuni = new javax.swing.JTextField();
        busca = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        pesquisa = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        qtde = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        f3 = new javax.swing.JLabel();
        esc = new javax.swing.JLabel();
        fX = new javax.swing.JLabel();
        insert = new javax.swing.JLabel();
        f4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        supervisor = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        fX1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        f2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        pageup = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        importar = new javax.swing.JLabel();
        importar_tecla = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jlabel18 = new javax.swing.JLabel();
        labelvendedor = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jLabel7.setText("jLabel7");

        jLabel17.setText("jLabel17");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados para Entrega"));

        jLabel18.setText("Nome:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel19.setText("Endereço:");

        jLabel20.setText("Bairro:");

        jLabel21.setText("Cidade/UF:");

        jLabel22.setText("Próximo:");

        jTextField2.setText("jTextField2");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("jButton1");

        jLabel23.setText("Comp.:");

        jTextField5.setText("jTextField5");

        jLabel24.setText("Telefone:");

        jTextField6.setText("jTextField6");

        jTextField7.setText("jTextField7");

        jCheckBox1.setText("Associar número telefonico ao contato.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel23)
                        .addComponent(jLabel21)
                        .addComponent(jLabel20)
                        .addComponent(jLabel19)
                        .addComponent(jLabel18)
                        .addComponent(jLabel22))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel24)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(jTextField7)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jCheckBox1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(400, 210, 390, 260);

        produtos.setBackground(new java.awt.Color(255, 255, 204));
        produtos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        produtos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        produtos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                produtosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(produtos);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 220, 360, 280);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel2.setText("R$");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 500, 66, 70);

        total.setEditable(false);
        total.setBackground(new java.awt.Color(255, 255, 204));
        total.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });
        getContentPane().add(total);
        total.setBounds(80, 500, 290, 64);

        mult.setEditable(false);
        mult.setBackground(new java.awt.Color(255, 255, 255));
        mult.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        mult.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mult.setText("Aguardando...");
        mult.setBorder(null);
        mult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multActionPerformed(evt);
            }
        });
        getContentPane().add(mult);
        mult.setBounds(40, 145, 220, 30);

        nomeprodutos.setEditable(false);
        nomeprodutos.setBackground(new java.awt.Color(255, 255, 255));
        nomeprodutos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        nomeprodutos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nomeprodutos.setText("Caixa Livre");
        nomeprodutos.setBorder(null);
        nomeprodutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeprodutosActionPerformed(evt);
            }
        });
        getContentPane().add(nomeprodutos);
        nomeprodutos.setBounds(280, 145, 480, 30);

        vuni.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        vuni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        vuni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vuniActionPerformed(evt);
            }
        });
        vuni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                vuniKeyPressed(evt);
            }
        });
        getContentPane().add(vuni);
        vuni.setBounds(480, 530, 159, 23);

        busca.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        busca.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaActionPerformed(evt);
            }
        });
        busca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                buscaFocusLost(evt);
            }
        });
        busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscaKeyTyped(evt);
            }
        });
        getContentPane().add(busca);
        busca.setBounds(400, 490, 380, 30);

        pesquisa.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pesquisa.setEnabled(false);
        pesquisa.setPreferredSize(null);
        pesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pesquisaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pesquisaKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(pesquisa);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(370, 350, 420, 130);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/gdoor+sistemas+concordia+sc+brasil__223EC2_1.jpg"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(400, 210, 390, 260);

        qtde.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtde.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtde.setText("01");
        qtde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtdeKeyPressed(evt);
            }
        });
        getContentPane().add(qtde);
        qtde.setBounds(700, 530, 90, 23);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Valor Unitário:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(260, 450, 100, 17);

        f3.setForeground(new java.awt.Color(255, 255, 255));
        f3.setText(" Finalizar");
        getContentPane().add(f3);
        f3.setBounds(40, 10, 70, 14);

        esc.setBackground(new java.awt.Color(255, 255, 255));
        esc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        esc.setForeground(new java.awt.Color(255, 255, 255));
        esc.setText("Sair");
        esc.setToolTipText("ESC - para fechar");
        esc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                escMouseClicked(evt);
            }
        });
        esc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                escKeyPressed(evt);
            }
        });
        getContentPane().add(esc);
        esc.setBounds(710, 10, 30, 15);

        fX.setText("F12 - Leitura X");
        getContentPane().add(fX);
        fX.setBounds(320, 10, 80, 14);

        insert.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        insert.setText("[INSERT = Quantidade]");
        getContentPane().add(insert);
        insert.setBounds(400, 480, 110, 10);

        f4.setBackground(new java.awt.Color(255, 255, 25));
        f4.setForeground(new java.awt.Color(255, 255, 255));
        f4.setText(" Cancelar");
        getContentPane().add(f4);
        f4.setBounds(140, 10, 60, 14);

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("CUPOM");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 180, 330, 20);

        supervisor.setForeground(new java.awt.Color(255, 255, 255));
        supervisor.setText("Supervisor");
        supervisor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supervisorMouseClicked(evt);
            }
        });
        getContentPane().add(supervisor);
        supervisor.setBounds(590, 10, 70, 14);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 204, 0));
        jLabel9.setText("AGUARDANDO COMANDO");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(140, 80, 520, 60);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("CAIXA LIVRE");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(130, 50, 520, 40);

        fX1.setText("F12 - Leitura X");
        getContentPane().add(fX1);
        fX1.setBounds(230, 10, 80, 14);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("F3");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(90, 20, 20, 14);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("F9");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(660, 20, 20, 14);

        f2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        f2.setForeground(new java.awt.Color(255, 255, 255));
        f2.setText("F2");
        getContentPane().add(f2);
        f2.setBounds(190, 20, 20, 14);

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Consulta");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(500, 10, 70, 14);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("F8");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(560, 20, 20, 14);

        jLabel15.setText("Valor Unitário:");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(390, 535, 80, 14);

        jLabel16.setText("Qtde.:");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(650, 535, 50, 14);

        pageup.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        pageup.setText("[PAGE UP = Valor Unitário]");
        getContentPane().add(pageup);
        pageup.setBounds(490, 480, 120, 10);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel8.setText("[DELETE = Apagar item]");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(590, 480, 90, 10);

        importar.setForeground(new java.awt.Color(255, 255, 255));
        importar.setText("Importar");
        importar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importarMouseClicked(evt);
            }
        });
        getContentPane().add(importar);
        importar.setBounds(420, 10, 60, 14);

        importar_tecla.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        importar_tecla.setForeground(new java.awt.Color(255, 255, 255));
        importar_tecla.setText("F5");
        getContentPane().add(importar_tecla);
        importar_tecla.setBounds(470, 20, 20, 14);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("ESC");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(740, 20, 20, 14);

        jlabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlabel18.setForeground(new java.awt.Color(255, 255, 255));
        jlabel18.setText("Vendedor: ");
        getContentPane().add(jlabel18);
        jlabel18.setBounds(10, 582, 60, 14);

        labelvendedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelvendedor.setForeground(new java.awt.Color(255, 255, 255));
        labelvendedor.setText("labelvendedor");
        getContentPane().add(labelvendedor);
        labelvendedor.setBounds(70, 582, 310, 14);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/image00.jpg"))); // NOI18N
        jLabel4.setText("Cupom Fiscal ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 0, 1024, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void importarComanda(){
        model.removeAllElements();
        limpa();
        vuni.setText("");
        model.removeAllElements();
        nomeprodutos.setText("Caixa Livre");
        mult.setText("Aguardando...");
        total.setText("0.00");
        busca.requestFocus();


        frameBuscarComanda frame = new frameBuscarComanda(null, true);
        frame.setVisible(true);
        if (frame.getAddPrincipal()){
            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            Main.comanda_mesa = frame.codigo;
            f.open("SELECT * FROM CMD_ITENS WHERE NUMERO = '"+frame.codigo+"' AND CANCELADO = '0'");
            while (f.next()){
                qtde.setText(df.format(Double.parseDouble(f.fieldbyname("qtd"))));
                vuni.setText(df.format(Double.parseDouble(f.fieldbyname("valor_unit"))));

                model.addElement(f.fieldbyname("codigo") + "   "+f.fieldbyname("descricao"));
                model.addElement("     "+qtde.getText()+" x "+vuni.getText()+"    "+df.format(multiplica()));
                total.setText(df.format(totalizador()));
                produtos.setModel(model);
                jScrollPane1.getVerticalScrollBar().setValue(produtos.getHeight()+50);
            }

            f.open("SELECT total as aux FROM CABCOMANDA WHERE COMANDA_MESA ='"+frame.codigo+"'");
            Float t = null;
            while(f.next()){
                total.setText( df.format(Double.parseDouble(f.fieldbyname("total"))));
            }
            qtde.setText("01");
            vuni.setText("");
        }
    }
    
    
    private void cancelaNovo(){
        limpa();
        jScrollPane2.setVisible(false);
        vuni.setText("");
        model.removeAllElements();
        nomeprodutos.setText("Caixa Livre");
        mult.setText("Aguardando...");
        total.setText("0.00");
    }
    
    private Float conversor(String texto){
        String v = texto.replace(".", "");
        return Float.parseFloat(v.replace(",", "."));
    }
    private void fecharCupom(){
        if (Main.fechacupom.equals("simplificado")){
            fecharCupom f = new fecharCupom(this, true);
            f.setDados(conversor(total.getText()),produtos);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            if (!f.getVoltar()){
                limpa();
                vuni.setText("");
                model.removeAllElements();
                nomeprodutos.setText("Caixa Livre");
                mult.setText("Aguardando...");
                total.setText("0.00");
            }
        }else{
            if (Main.fechacupom.equals("extendido")){
                frameFecharCupomExtendido f = new frameFecharCupomExtendido(this, true);
                f.setDados(conversor(total.getText()),produtos);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                if (!f.getVoltar()){
                    limpa();
                    vuni.setText("");
                    model.removeAllElements();
                    nomeprodutos.setText("Caixa Livre");
                    mult.setText("Aguardando...");
                    total.setText("0.00");
                }
            }
            
            
                
        }
        
        busca.requestFocus();
    }

    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalActionPerformed

    private void nomeprodutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeprodutosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeprodutosActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode()==113)
                System.exit(0);// TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private void buscaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaKeyPressed
        
        if (evt.getKeyCode() == 40){
            int tam = pesquisa.getModel().getSize();
            int i = pesquisa.getSelectedIndex();
            i++;
            if (i > tam-1){
                pesquisa.setSelectedIndex(0);
                pesquisa.ensureIndexIsVisible(0);
            }else{
                pesquisa.setSelectedIndex(i);
                pesquisa.ensureIndexIsVisible(i);
            }
            return;
        }
        if (evt.getKeyCode() == 38 ){
            int tam = pesquisa.getModel().getSize();
            int i = pesquisa.getSelectedIndex();
            i--;
            if (i < 0){
                pesquisa.setSelectedIndex(tam-1);
                pesquisa.ensureIndexIsVisible(tam-1);
            }else{
                pesquisa.setSelectedIndex(i);
                pesquisa.ensureIndexIsVisible(i);
            }
            return;
        }
        if (evt.getKeyCode() == 10 && !visivel && !busca.getText().equals("")){
            preencheDados(busca.getText());
            if (addElementos()){
                mult.setText(qtde.getText()+" x "+ vuni.getText());
                busca.setText("");
                vuni.setText("");
                qtde.setText("01");
                busca.requestFocus();
            }
            return;
        }
        if (evt.getKeyCode()== 10 && pesquisa.getSelectedIndex() >= 0 && visivel){ 
            String dados = pesquisa.getSelectedValue().toString();
            if (dados.equals("")){
                return;
            }
            String cut[] = dados.split(" - ");
            cod[indice] = Integer.parseInt(cut[0]);

            System.out.println(cut[0]);
            preencheDados(cut[0]);
            jScrollPane2.setVisible(false);
            visivel = false;
            limpa();
            indice++;
        }

        if (evt.getKeyCode() == 10 && model.getSize() > 0 && !visivel && busca.getText().equals("")){
            fecharCupom();
        }

        
        /*if (evt.getKeyCode() == 10 && indice > 0 && busca.getText().length() == 0){
            System.out.println("Encerrando compra");
        }*/

    }//GEN-LAST:event_buscaKeyPressed

    private void limpa(){
        //busca.setText("");
        //qtde.setText("01");
        //vuni.setText("");
        busca.requestFocus();
    }

    private boolean addElementos(){//adiciona os produtos a lista de compra dos produtos
        
        bd b = new bd();
        b.connect();
        mySql f = new mySql(b.conn);
        String pesquisa = busca.getText();

        if (busca.getText().charAt(0) == '2' && busca.getText().length() == 13){
            pesquisa = busca.getText().substring(0, 7);
        }
        System.out.println(pesquisa);
        f.open("SELECT codigo,descricao FROM ESTOQUE WHERE codigo = '"+pesquisa+"' OR barras ='"+pesquisa+"'");
       
            while (f.next()){
                model.addElement(f.fieldbyname("codigo") + "   "+f.fieldbyname("descricao"));
                model.addElement("     "+qtde.getText()+" x "+vuni.getText()+"    "+df.format(multiplica()));
                total.setText(df.format(totalizador()));
                produtos.setModel(model);
                jScrollPane1.getVerticalScrollBar().setValue(produtos.getHeight()+50);
                return true;
            }

        if (!f.next()){
            nomeprodutos.setText("Produto inexistente");
            mult.setText("Aguardando...");
            busca.setText("");
            return false;
        }

        return false;
    }
    private Float totalizador(String aux){
        String auxtotal = total.getText().replace(".", "");
        Float total = conversor(auxtotal);
        total += conversor(aux);
        return total;
    }

    private Float totalizador(){
        Float ptotal  = multiplica();
        Float total = conversor(this.total.getText());
        total += ptotal;
        return total;
    }

    private Float multiplica(){
        Float q = conversor(qtde.getText());
        Float u = conversor(vuni.getText());
        Float total = q * u;
        System.out.println(total);
        return total;
    }
    
    private void preencheDados(String cod){
        if (setDadosBalanca(cod)){
            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            f.open("SELECT codigo, descricao, preco_venda FROM ESTOQUE WHERE codigo = '"+cod+"' OR barras = '"+cod+"' ");
            while (f.next()){
                nomeprodutos.setText(f.fieldbyname("DESCRICAO"));
                if (vuni.getText().equals("0,00") || vuni.getText().equals(""))
                    vuni.setText(df.format(Double.parseDouble(f.fieldbyname("PRECO_VENDA"))));
                busca.setText(f.fieldbyname("codigo"));
            }
            mult.setText(qtde.getText()+" x "+vuni.getText());
            
            
        }
    }

    private boolean setDadosBalanca(String cod){
        DecimalFormat p = new DecimalFormat("#0.000");
        char c[] = cod.toCharArray();
        if (c[0] == '2' && c.length == 13){

            String codigo = cod.substring(0, 7);
            String peso = "";

            for (int i=7;i<12;i++){
                if (i == 9){
                    peso +="."+c[i];
                }else
                    peso +=""+c[i];
            }

            bd b = new bd();
            b.connect();
            mySql f = new mySql(b.conn);
            f.open("SELECT descricao, preco_venda, codigo FROM ESTOQUE WHERE codigo = '"+codigo+"' OR barras ='"+codigo+"'");
            while (f.next()){
                nomeprodutos.setText(f.fieldbyname("DESCRICAO"));
                vuni.setText(df.format(Double.parseDouble(f.fieldbyname("PRECO_VENDA"))));
                busca.setText(f.fieldbyname("codigo"));
            }
            qtde.setText(p.format(Double.parseDouble(peso)));
            mult.setText(p.format(Double.parseDouble(peso))+" x "+vuni.getText());
            return false;


        }
        return true;
    }
    private void buscaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaFocusLost
       
    }//GEN-LAST:event_buscaFocusLost

    private void pesquisaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaKeyTyped
        
    }//GEN-LAST:event_pesquisaKeyTyped

    private void pesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaKeyReleased
              // TODO add your handling code here:
    }//GEN-LAST:event_pesquisaKeyReleased

    private void buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaKeyTyped
                   // TODO add your handling code here:
    }//GEN-LAST:event_buscaKeyTyped

    private void buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaKeyReleased

    }//GEN-LAST:event_buscaKeyReleased

    private void qtdeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtdeKeyPressed
        if(evt.getKeyCode() == 10 && !qtde.getText().equals("")){
            if (!busca.getText().equals("")){
                mult.setText(qtde.getText()+" x "+vuni.getText());
            }
            busca.requestFocus();
        }// TODO add your handling code here:
        return;
    }//GEN-LAST:event_qtdeKeyPressed

    private void produtosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_produtosKeyPressed
        String valor = "";
        String recorte[];
        
        
        if (evt.getKeyCode()==127){
            if (!Main.useritemCancelar){
                nivelUsuario n = new nivelUsuario();
                if (!n.checkAdmin("Item - Cancelar"))
                    return;
            }
                
                try{
                    if (produtos.getSelectedIndex() != -1){
                        if (produtos.getSelectedIndex()%2 == 1){
                           valor = produtos.getSelectedValue().toString();
                           model.removeElementAt(produtos.getSelectedIndex()-1);
                           model.removeElementAt(produtos.getSelectedIndex());
                           produtos.setModel(model);
                           recorte = valor.split(" ");
                           Float tot = conversor(total.getText()) - conversor(recorte[recorte.length -1]);
                           total.setText(df.format(tot));
                        }else{
                           valor = model.getElementAt(produtos.getSelectedIndex()+1).toString();
                           model.removeElementAt(produtos.getSelectedIndex()+1);
                           model.removeElementAt(produtos.getSelectedIndex());
                           produtos.setModel(model);
                           recorte = valor.split(" ");
                           Double tot = Double.parseDouble(total.getText().replace(",", ".")) - Double.parseDouble( recorte[recorte.length -1].replace(",", ".") );
                           total.setText(df.format(tot));
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
        }
    }//GEN-LAST:event_produtosKeyPressed

    private void multActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_multActionPerformed

    private void escKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_escKeyPressed
                              // TODO add your handling code here:
    }//GEN-LAST:event_escKeyPressed

    private void escMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_escMouseClicked
        if (!Main.usercupomCancelar){
                        nivelUsuario n = new nivelUsuario();
                        if (!n.checkAdmin("Cupom - Cancelar"))
                            return;
                     }
        if (JOptionPane.showConfirmDialog(null, "Deseja realmente sair?") == 0)
                  System.exit(0);
            else
                  busca.requestFocus();          // TODO add your handling code here:
    }//GEN-LAST:event_escMouseClicked

    private void vuniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vuniKeyPressed
        if((evt.getKeyCode() == 10 && !vuni.getText().equals(""))){
            if (!busca.getText().equals("")){
                mult.setText(qtde.getText()+" x "+vuni.getText());
            }
            busca.requestFocus();
        }// TODO add your handling code here:

        return;
}//GEN-LAST:event_vuniKeyPressed

    private void supervisorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supervisorMouseClicked
        jPopupMenu1 = new JPopupMenu();

        /*JMenuItem fechar_caixa = new JMenuItem("Fechar caixa");
        jPopupMenu1.add(fechar_caixa);
        fechar_caixa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicou na Opção 1");
            }
        });*/

        JMenuItem especie = new JMenuItem("Cadastrar Especie");
        jPopupMenu1.add(especie);
        especie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                frameCadastrarEspecie f = new frameCadastrarEspecie(null, true);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
                
            }
        });
        
        
        JMenuItem relatorio = new JMenuItem("Relatório");
        jPopupMenu1.add(relatorio);
        relatorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FrameRelatorios f = new FrameRelatorios(null, true);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                
            }
        });
        

        JMenuItem cancelar_venda = new JMenuItem("Cancelar última venda");
        jPopupMenu1.add(cancelar_venda);
        cancelar_venda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Main.usercupomCancelar){
                            nivelUsuario n = new nivelUsuario();
                            if (!n.checkAdmin("Cupom - Cancelar"))
                                return;
                        }
                cancelarUltimaVenda f = new cancelarUltimaVenda(null, true);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
        
        //JCheckBoxMenuItem imprimir = new JCheckBoxMenuItem("Imprimir cupom");
        JMenuItem leiturax = new JMenuItem("Leitura X");
        jPopupMenu1.add(leiturax);
        leiturax.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Main.usercupomCancelar){
                            nivelUsuario n = new nivelUsuario();
                            if (!n.checkAdmin("Cupom - Cancelar"))
                                return;
                        }
                FrameLeituraX f = new FrameLeituraX(null, true);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });

      
        jPopupMenu1.add(cancelar_venda);
        jPopupMenu1.add(leiturax);
        //jPopupMenu1.add(imprimir);
        


        jPopupMenu1.show(evt.getComponent(),evt.getX() , evt.getY());

    }//GEN-LAST:event_supervisorMouseClicked

    private void importarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importarMouseClicked
        importarComanda();
    }//GEN-LAST:event_importarMouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscaActionPerformed

    private void vuniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vuniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vuniActionPerformed

    private void fechar_caixaMouseClicked(java.awt.event.MouseEvent evt) {
        System.out.print("clicou");
    }


    private Double setCorrige(Double valor){
        String v = ""+valor;
        v = v.replace(".", ",");
        char[] val = v.toCharArray();
        int tam = val.length;
        val[tam-4] = '.';
        return Double.parseDouble(val.toString());
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new framePrincipal().setVisible(true);
                } catch (BadLocationException ex) {
                    Logger.getLogger(framePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField busca;
    private javax.swing.JLabel esc;
    private javax.swing.JLabel f2;
    private javax.swing.JLabel f3;
    private javax.swing.JLabel f4;
    private javax.swing.JLabel fX;
    private javax.swing.JLabel fX1;
    private javax.swing.JLabel importar;
    private javax.swing.JLabel importar_tecla;
    private javax.swing.JLabel insert;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel jlabel18;
    private javax.swing.JLabel labelvendedor;
    private javax.swing.JTextField mult;
    private javax.swing.JTextField nomeprodutos;
    private javax.swing.JLabel pageup;
    private javax.swing.JList pesquisa;
    private javax.swing.JList produtos;
    private javax.swing.JTextField qtde;
    private javax.swing.JLabel supervisor;
    private javax.swing.JTextField total;
    private javax.swing.JTextField vuni;
    // End of variables declaration//GEN-END:variables

}
