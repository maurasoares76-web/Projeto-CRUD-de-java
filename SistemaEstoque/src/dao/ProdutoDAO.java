package dao;

import modelo.Produto;
import java.sql.*;
import java.util.*;

public class ProdutoDAO {

    public boolean inserir(Produto p) {
        String sql = "INSERT INTO produtos (nome, categoria, preco, quantidade) VALUES (?,?,?,?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPreco());
            ps.setInt(4, p.getQuantidade());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public boolean atualizar(Produto p) {
        String sql = "UPDATE produtos SET nome=?, categoria=?, preco=?, quantidade=? WHERE id=?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPreco());
            ps.setInt(4, p.getQuantidade());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id=?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";
        try (Connection con = Conexao.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"),
                    rs.getString("categoria"), rs.getDouble("preco"), rs.getInt("quantidade")));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    public List<Produto> estoqueBaixo(int minimo) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE quantidade <= ? ORDER BY quantidade";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, minimo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"),
                    rs.getString("categoria"), rs.getDouble("preco"), rs.getInt("quantidade")));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }

    public List<Produto> buscarPorNome(String nome) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"),
                    rs.getString("categoria"), rs.getDouble("preco"), rs.getInt("quantidade")));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return lista;
    }
}