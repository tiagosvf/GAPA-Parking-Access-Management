package com.example.a6100331.softinsa_gestao;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tiago on 03/05/2017.
 */

public class Lista_Eventos {

    public static ArrayList<Evento> lista;

    public static void load() {
        lista = new ArrayList<>();
    }

    public static void adicionar(Evento evento) {
        Log.d("Teste adicionar.", "" + evento.toString());
        lista.add(evento);
    }

    public static void limpar() {
        lista.clear();
    }

    public static Evento get(int i) {
        return lista.get(i);
    }
}
