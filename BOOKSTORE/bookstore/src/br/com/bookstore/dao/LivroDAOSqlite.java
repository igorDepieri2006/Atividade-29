package br.com.bookstore.dao;

import br.com.bookstore.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroDAOSqlite implements LivroDAO {

    private static final String URL = "jdbc:sqlite:bookstore.db";

    public LivroDAOSqlite() {
        criarTabelaSeNaoExistir();
    }

    // Inicialização Determinística
    private void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                preco REAL NOT NULL
            );
        """;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Mapeamento Centralizado
    private Livro mapearLinha(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        double preco = rs.getDouble("preco");
        return new Livro(id, titulo, autor, preco);
    }

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, preco) VALUES (?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setDouble(3, livro.getPreco());
            
            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                try (ResultSet chaves = stmt.getGeneratedKeys()) {
                    if (chaves.next()) {
                        livro.setId(chaves.getLong(1)); // Captura do ID Autoincrement
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao salvar livro: " + e.getMessage());
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String sql = "SELECT id, titulo, autor, preco FROM livros WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar livro por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, preco FROM livros ORDER BY titulo;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapearLinha(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar todos os livros: " + e.getMessage());
        }
        return lista;
    }
}