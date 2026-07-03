package modelo;

public class Produto {

    private int    id;
    private String nome;
    private String categoria;
    private double preco;
    private int    quantidade;

    public Produto() {}

    public Produto(int id, String nome, String categoria, double preco, int quantidade) {
        this.id         = id;
        this.nome       = nome;
        this.categoria  = categoria;
        this.preco      = preco;
        this.quantidade = quantidade;
    }

    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }
    public String getNome()             { return nome; }
    public void   setNome(String v)     { this.nome = v; }
    public String getCategoria()        { return categoria; }
    public void   setCategoria(String v){ this.categoria = v; }
    public double getPreco()            { return preco; }
    public void   setPreco(double v)    { this.preco = v; }
    public int    getQuantidade()       { return quantidade; }
    public void   setQuantidade(int v)  { this.quantidade = v; }
}