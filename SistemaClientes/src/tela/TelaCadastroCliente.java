package tela;

import dao.ClienteDAO;
import modelo.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroCliente extends JFrame {

    private JTextField txtId, txtNome, txtCpf, txtTelefone, txtEmail, txtCidade, txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ClienteDAO dao = new ClienteDAO();

    public TelaCadastroCliente() {
        setTitle("Sistema 1 — Cadastro de Clientes");
        setSize(820, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        txtId       = new JTextField(5);  txtId.setEditable(false);
        txtNome     = new JTextField(20);
        txtCpf      = new JTextField(14);
        txtTelefone = new JTextField(15);
        txtEmail    = new JTextField(25);
        txtCidade   = new JTextField(20);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        String[] labels = {"ID:", "Nome:", "CPF:", "Telefone:", "E-mail:", "Cidade:"};
        JTextField[] fields = {txtId, txtNome, txtCpf, txtTelefone, txtEmail, txtCidade};
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            pnlForm.add(new JLabel(labels[i]), g);
            g.gridx = 1; g.weightx = 1;
            pnlForm.add(fields[i], g);
        }

        JButton btnNovo    = new JButton("Novo");
        JButton btnSalvar  = new JButton("Salvar");
        JButton btnEditar  = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        btnSalvar .setBackground(new Color(34,139,34));   btnSalvar .setForeground(Color.WHITE);
        btnEditar .setBackground(new Color(30,100,200));  btnEditar .setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(180,30,30));   btnExcluir.setForeground(Color.WHITE);

        JPanel pnlBotoes = new JPanel(new FlowLayout());
        pnlBotoes.add(btnNovo); pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnEditar); pnlBotoes.add(btnExcluir);

        JPanel pnlEsq = new JPanel(new BorderLayout());
        pnlEsq.add(pnlForm, BorderLayout.CENTER);
        pnlEsq.add(pnlBotoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(
            new String[]{"ID","Nome","CPF","Telefone","E-mail","Cidade"}, 0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        txtBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnListar = new JButton("Listar Todos");
        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusca.add(new JLabel("Buscar:")); pnlBusca.add(txtBusca);
        pnlBusca.add(btnBuscar); pnlBusca.add(btnListar);

        JPanel pnlDir = new JPanel(new BorderLayout());
        pnlDir.add(pnlBusca, BorderLayout.NORTH);
        pnlDir.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlEsq, pnlDir);
        split.setDividerLocation(300);
        add(split, BorderLayout.CENTER);

        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> {
            Cliente c = coletar();
            if (dao.inserir(c)) { JOptionPane.showMessageDialog(this,"Salvo!"); limpar(); carregarTabela(dao.listarTodos()); }
        });
        btnEditar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this,"Selecione um cliente."); return; }
            Cliente c = coletar(); c.setId(Integer.parseInt(txtId.getText()));
            if (dao.atualizar(c)) { JOptionPane.showMessageDialog(this,"Atualizado!"); limpar(); carregarTabela(dao.listarTodos()); }
        });
        btnExcluir.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            if (JOptionPane.showConfirmDialog(this,"Excluir?","",JOptionPane.YES_NO_OPTION)==0) {
                dao.excluir(Integer.parseInt(txtId.getText())); limpar(); carregarTabela(dao.listarTodos());
            }
        });
        btnBuscar.addActionListener(e -> carregarTabela(dao.buscarPorNome(txtBusca.getText())));
        btnListar.addActionListener(e -> { txtBusca.setText(""); carregarTabela(dao.listarTodos()); });
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int r = tabela.getSelectedRow();
            if (r >= 0) { txtId.setText(modeloTabela.getValueAt(r,0).toString()); txtNome.setText(modeloTabela.getValueAt(r,1).toString()); txtCpf.setText(modeloTabela.getValueAt(r,2).toString()); txtTelefone.setText(modeloTabela.getValueAt(r,3).toString()); txtEmail.setText(modeloTabela.getValueAt(r,4).toString()); txtCidade.setText(modeloTabela.getValueAt(r,5).toString()); }
        });
        carregarTabela(dao.listarTodos());
    }

    private Cliente coletar() {
        Cliente c = new Cliente();
        c.setNome(txtNome.getText()); c.setCpf(txtCpf.getText());
        c.setTelefone(txtTelefone.getText()); c.setEmail(txtEmail.getText()); c.setCidade(txtCidade.getText());
        return c;
    }
    private void limpar() { for(JTextField f : new JTextField[]{txtId,txtNome,txtCpf,txtTelefone,txtEmail,txtCidade}) f.setText(""); tabela.clearSelection(); }
    private void carregarTabela(List<Cliente> lista) {
        modeloTabela.setRowCount(0);
        for (Cliente c : lista) modeloTabela.addRow(new Object[]{c.getId(),c.getNome(),c.getCpf(),c.getTelefone(),c.getEmail(),c.getCidade()});
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new TelaCadastroCliente().setVisible(true)); }
}