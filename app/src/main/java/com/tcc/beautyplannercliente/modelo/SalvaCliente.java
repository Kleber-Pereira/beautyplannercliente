package com.tcc.beautyplannercliente.modelo;

public class SalvaCliente {
    private String nome;
    private String contato;
    private String email;


    public SalvaCliente(){}

    public SalvaCliente(String nome, String contato, String email) {
        this.nome = nome;
        this.contato = contato;
        this.email = email;

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
