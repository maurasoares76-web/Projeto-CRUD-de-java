package modelo;

public class Cliente {

    private int    id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String cidade;

    public Cliente() {}

    public Cliente(int id, String nome, String cpf, String telefone, String email, String cidade) {
        this.id       = id;
        this.nome     = nome;
        this.cpf      = cpf;
        this.telefone = telefone;
        this.email    = email;
        this.cidade   = cidade;
    }

    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }
    public String getNome()            { return nome; }
    public void   setNome(String v)    { this.nome = v; }
    public String getCpf()             { return cpf; }
    public void   setCpf(String v)     { this.cpf = v; }
    public String getTelefone()        { return telefone; }
    public void   setTelefone(String v){ this.telefone = v; }
    public String getEmail()           { return email; }
    public void   setEmail(String v)   { this.email = v; }
    public String getCidade()          { return cidade; }
    public void   setCidade(String v)  { this.cidade = v; }
}