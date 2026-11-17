package br.com.bookstore.model;

public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private double preco;

    // Construtor Completo (usado para hidratar tuplas vindas do banco de dados)
    public Livro(Long id, String titulo, String autor, double preco) {
        validarInvariantes(titulo, autor, preco);
        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.preco = preco;
    }

    // Construtor de Negócio (atribui null ao id para novos registros)
    public Livro(String titulo, String autor, double preco) {
        this(null, titulo, autor, preco);
    }

    // Blindagem de Invariantes de Domínio
    private void validarInvariantes(String titulo, String autor, double preco) {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio e deve ter pelo menos 2 caracteres.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor não pode ser nulo ou vazio.");
        }
        if (preco <= 0.0) {
            throw new IllegalArgumentException("O preço de venda deve ser estritamente positivo");
        }
    }

    public Long getId() {
        return id;
    }

    // Único setter permitido para sincronizar a PK gerada pelo banco
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Título: %-25s | Autor: %-20s | Preço: R$ %7.2f",
                id != null ? id : 0, titulo, autor, preco);
    }
}