package com.example.a6100331.softinsa_gestao;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 6100331 on 02-06-2017.
 */

public class Lista_Portoes {
    public static ArrayList<Portao> lista;

    public static void load() {
        lista = new ArrayList<>();
    }

    public static void adicionar(Portao portao) {
        Log.d("Teste adicionar.", "" + portao.toString());
        lista.add(portao);
    }

    public static void limpar() {
        lista.clear();
    }

    public static Portao get(int i) {
        return lista.get(i);
    }
}
