package dao;

import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean inserir(Cliente c) {
        String sql = "INSERT INTO clientes (nome, cpf, telefone, email, cidade) VALUES (?,?,?,?,?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCidade());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Cliente c) {
        String sql = "UPDATE clientes SET nome=?, cpf=?, telefone=?, email=?, cidade=? WHERE id=?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCidade());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";
        try (Connection con = Conexao.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
                    rs.getString("telefone"), rs.getString("email"), rs.getString("cidade")
                ));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nome LIKE ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Cliente(rs.getInt("id"), rs.getString("nome"),
                    rs.getString("cpf"), rs.getString("telefone"),
                    rs.getString("email"), rs.getString("cidade")));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }
}