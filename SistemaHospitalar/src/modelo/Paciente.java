package modelo;

import java.util.Date;

public class Paciente {
    private int idPaciente;
    private String nome;
    private String endereco;
    private Date dataNascimento;
    private String telefone;
    private String cpf;
    private String rg;
    private int idConvenio;
    
    // Getters e Setters
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    
    public int getIdConvenio() { return idConvenio; }
    public void setIdConvenio(int idConvenio) { this.idConvenio = idConvenio; }
}