package com.example.a6100331.softinsa_gestao;

/**
 * Created by 6100331 on 02-06-2017.
 */

public class Portao {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Portao(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int id;
    public String nome;

}
