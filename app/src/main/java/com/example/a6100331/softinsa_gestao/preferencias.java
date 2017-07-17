package com.example.a6100331.softinsa_gestao;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Tiago on 03/05/2017.
 */

public class preferencias {



    public static void guardaId(int id, String tipo) {
        SharedPreferences idx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        SharedPreferences.Editor editor = idx.edit();
        editor.putInt("ID_user", id);
        editor.putString("Tipo_user",tipo);
        Log.d("Tipo:",""+tipo);
        editor.commit();

    }
    public static void guardaLink(String link) {
        SharedPreferences idx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        SharedPreferences.Editor editor = idx.edit();
        editor.putString("Link", "http://"+link);

        editor.commit();

    }

    public static void guardaPW(String pw) {
        SharedPreferences idx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        SharedPreferences.Editor editor = idx.edit();
        editor.putString("PW", pw);

        editor.commit();

    }

    public static int recebeId() {
        SharedPreferences idx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        return idx.getInt("ID_user", -1);

    }
    public static String recebePW() {
        SharedPreferences pwx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        return pwx.getString("PW", "");

    }

    public static String recebeTipo() {
        SharedPreferences tipox = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        return tipox.getString("Tipo_user", "Utilizador");

    }
    public static String recebeLink() {
        SharedPreferences linkx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        return linkx.getString("Link", "");

    }

    public static void removeId() {

        SharedPreferences idx = PreferenceManager.getDefaultSharedPreferences(Pagina_Login.context());
        SharedPreferences.Editor editor = idx.edit();
        editor.remove("ID_user");
        editor.remove("Tipo_user");
       // editor.clear();
        editor.commit();
    }
}
