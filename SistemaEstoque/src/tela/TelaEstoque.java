package tela;

import dao.ProdutoDAO;
import modelo.Produto;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TelaEstoque extends JFrame {

    private JTextField txtId, txtNome, txtCategoria, txtPreco, txtQtd, txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ProdutoDAO dao = new ProdutoDAO();

    public TelaEstoque() {
        setTitle("Sistema 2 — Controle de Estoque");
        setSize(820, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        txtId        = new JTextField(5); txtId.setEditable(false);
        txtNome      = new JTextField(20);
        txtCategoria = new JTextField(15);
        txtPreco     = new JTextField(10);
        txtQtd       = new JTextField(8);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4,6,4,6); g.fill = GridBagConstraints.HORIZONTAL;
        String[] lbs = {"ID:","Nome:","Categoria:","Preço:","Qtd:"};
        JTextField[] fs = {txtId,txtNome,txtCategoria,txtPreco,txtQtd};
        for(int i=0;i<lbs.length;i++){g.gridx=0;g.gridy=i;g.weightx=0;pnlForm.add(new JLabel(lbs[i]),g);g.gridx=1;g.weightx=1;pnlForm.add(fs[i],g);}

        JButton btnNovo=new JButton("Novo"),btnSalvar=new JButton("Salvar"),
                btnEditar=new JButton("Editar"),btnExcluir=new JButton("Excluir"),
                btnAlerta=new JButton("Estoque Baixo");
        btnSalvar.setBackground(new Color(34,139,34)); btnSalvar.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(30,100,200)); btnEditar.setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(180,30,30)); btnExcluir.setForeground(Color.WHITE);
        btnAlerta.setBackground(new Color(200,140,0)); btnAlerta.setForeground(Color.WHITE);
        JPanel pnlBt = new JPanel(new FlowLayout());
        for(JButton b:new JButton[]{btnNovo,btnSalvar,btnEditar,btnExcluir,btnAlerta}) pnlBt.add(b);
        JPanel pnlEsq=new JPanel(new BorderLayout()); pnlEsq.add(pnlForm,BorderLayout.CENTER); pnlEsq.add(pnlBt,BorderLayout.SOUTH);

        modeloTabela=new DefaultTableModel(new String[]{"ID","Nome","Categoria","Preço","Qtd"},0){public boolean isCellEditable(int r,int c){return false;}};
        tabela=new JTable(modeloTabela);
        tabela.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int row,int col){
                Component c=super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                try{int q=Integer.parseInt(modeloTabela.getValueAt(row,4).toString());
                if(!sel)c.setBackground(q==0?new Color(255,200,200):Color.WHITE);}catch(Exception e){}
                return c;}});

        txtBusca=new JTextField(20);
        JButton btnBuscar=new JButton("Buscar"),btnListar=new JButton("Listar Todos");
        JPanel pnlBusca=new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusca.add(new JLabel("Buscar:")); pnlBusca.add(txtBusca); pnlBusca.add(btnBuscar); pnlBusca.add(btnListar);
        JPanel pnlDir=new JPanel(new BorderLayout()); pnlDir.add(pnlBusca,BorderLayout.NORTH); pnlDir.add(new JScrollPane(tabela),BorderLayout.CENTER);
        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnlEsq,pnlDir); split.setDividerLocation(290);
        add(split,BorderLayout.CENTER);

        btnNovo.addActionListener(e->limpar());
        btnSalvar.addActionListener(e->{try{Produto p=coletar();if(dao.inserir(p)){JOptionPane.showMessageDialog(this,"Salvo!");limpar();carregarTabela(dao.listarTodos());}}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Preço e Qtd devem ser números.");}});
        btnEditar.addActionListener(e->{if(txtId.getText().isEmpty())return;try{Produto p=coletar();p.setId(Integer.parseInt(txtId.getText()));if(dao.atualizar(p)){JOptionPane.showMessageDialog(this,"Atualizado!");limpar();carregarTabela(dao.listarTodos());}}catch(NumberFormatException ex){}});
        btnExcluir.addActionListener(e->{if(txtId.getText().isEmpty())return;if(JOptionPane.showConfirmDialog(this,"Excluir?","",JOptionPane.YES_NO_OPTION)==0){dao.excluir(Integer.parseInt(txtId.getText()));limpar();carregarTabela(dao.listarTodos());}});
        btnAlerta.addActionListener(e->carregarTabela(dao.estoqueBaixo(5)));
        btnBuscar.addActionListener(e->carregarTabela(dao.buscarPorNome(txtBusca.getText())));
        btnListar.addActionListener(e->{txtBusca.setText("");carregarTabela(dao.listarTodos());});
        tabela.getSelectionModel().addListSelectionListener(e->{int r=tabela.getSelectedRow();if(r>=0){txtId.setText(modeloTabela.getValueAt(r,0).toString());txtNome.setText(modeloTabela.getValueAt(r,1).toString());txtCategoria.setText(modeloTabela.getValueAt(r,2).toString());txtPreco.setText(modeloTabela.getValueAt(r,3).toString());txtQtd.setText(modeloTabela.getValueAt(r,4).toString());}});
        carregarTabela(dao.listarTodos());
    }

    private Produto coletar(){Produto p=new Produto();p.setNome(txtNome.getText());p.setCategoria(txtCategoria.getText());p.setPreco(Double.parseDouble(txtPreco.getText().replace(",",".")));p.setQuantidade(Integer.parseInt(txtQtd.getText()));return p;}
    private void limpar(){for(JTextField f:new JTextField[]{txtId,txtNome,txtCategoria,txtPreco,txtQtd})f.setText("");tabela.clearSelection();}
    private void carregarTabela(List<Produto> lista){modeloTabela.setRowCount(0);for(Produto p:lista)modeloTabela.addRow(new Object[]{p.getId(),p.getNome(),p.getCategoria(),String.format("%.2f",p.getPreco()),p.getQuantidade()});}
    public static void main(String[] args){SwingUtilities.invokeLater(()->new TelaEstoque().setVisible(true));}
}