package br.com.bookstore.main;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("📚 STARLOG BOOKSTORE • INICIALIZANDO SISTEMA");
        System.out.println("==================================================");

        // 1. Instanciação aplicando DIP (Inversão de Dependência)
        LivroDAO dao = new LivroDAOSqlite();

        // 2. Persistir 3 obras literárias
        System.out.println("\n--- 2. PERSISTINDO OBRAS LITERÁRIAS ---");
        Livro livro1 = new Livro("Clean Code", "Robert C. Martin", 89.90);
        Livro livro2 = new Livro("Design Patterns", "Erich Gamma", 120.00);
        Livro livro3 = new Livro("Refactoring", "Martin Fowler", 99.50);

        dao.salvar(livro1);
        dao.salvar(livro2);
        dao.salvar(livro3);

        System.out.println("✅ Cadastrados com sucesso! IDs gerados:");
        System.out.println("-> " + livro1);
        System.out.println("-> " + livro2);
        System.out.println("-> " + livro3);

        // 3. Consultar o livro de ID 2 usando ifPresentOrElse
        System.out.println("\n--- 3. CONSULTANDO LIVRO DE ID 2 ---");
        dao.buscarPorId(2L).ifPresentOrElse(
            livro -> System.out.println("✅ Encontrado: " + livro),
            () -> System.out.println("⚠️ Livro não encontrado.")
        );

        // 4. Consultar um ID inexistente (999L) demonstrando segurança contra NullPointerException
        System.out.println("\n--- 4. CONSULTANDO ID INEXISTENTE (999L) ---");
        dao.buscarPorId(999L).ifPresentOrElse(
            livro -> System.out.println("✅ Encontrado: " + livro),
            () -> System.out.println("⚠️ Aviso amigável: Nenhum livro cadastrado com o ID 999 (Zero NullPointerException!).")
        );

        // 5. Chamar listarTodos() e imprimir com cabeçalho tabular
        System.out.println("\n--- 5. RELATÓRIO GERAL (LISTAR TODOS) ---");
        List<Livro> livros = dao.listarTodos();
        System.out.println("==================================================================================");
        System.out.println(String.format("%-5s | %-25s | %-20s | %s", "ID", "TÍTULO", "AUTOR", "PREÇO"));
        System.out.println("==================================================================================");
        for (Livro l : livros) {
            System.out.println(String.format("%-5d | %-25s | %-20s | R$ %7.2f",
                    l.getId(), l.getTitulo(), l.getAutor(), l.getPreco()));
        }
        System.out.println("==================================================================================");
        System.out.println("Total de livros no acervo: " + livros.size());
    }
}