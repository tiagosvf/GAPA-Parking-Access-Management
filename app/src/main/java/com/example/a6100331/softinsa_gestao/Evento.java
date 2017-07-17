package com.example.a6100331.softinsa_gestao;

/**
 * Created by Tiago on 03/05/2017.
 */

public class Evento {
    public int id;
    public String tipo;

    public String getNome_utilizador() {
        return nome_utilizador;
    }

    public void setNome_utilizador(String nome_utilizador) {
        this.nome_utilizador = nome_utilizador;
    }

    public String nome_utilizador;



    public int id_utilizador;

    public int getId() {
        return id;
    }

    public Evento(int id, String tipo, String nome_utilizador, int id_utilizador, String data_hora, String descricao, String portao) {
        this.id = id;
        this.tipo = tipo;
        this.nome_utilizador = nome_utilizador;
        this.id_utilizador = id_utilizador;
        this.data_hora = data_hora;
        this.descricao = descricao;
        this.portao = portao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(int id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPortao() {
        return portao;
    }

    public void setPortao(String portao) {
        this.portao = portao;
    }

    public String data_hora;
    public String descricao;
    public String portao;


}
