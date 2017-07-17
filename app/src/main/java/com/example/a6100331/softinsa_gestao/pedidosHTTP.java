package com.example.a6100331.softinsa_gestao;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tiago on 28/04/2017.
 */


public class pedidosHTTP {


    public static String teste() {

        try {
           String link = Link.getLink();
            URL url = new URL(link + "/mobilenome.aspx");
            String pagina = "";
            HttpURLConnection urlC = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlC.getInputStream());

            pagina = readStream(in);
            urlC.disconnect();
            return pagina;

        } catch (Exception ex) {
            Log.d("Erro", "Erro aqui", ex);
            return "";
        }


    }


    public static String listar(String paginax) {

        try {
             String link = Link.getLink();
            URL url = new URL(link + paginax);
            Log.d("url:",""+url);
            String pagina = "";
            HttpURLConnection urlC = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlC.getInputStream());

            pagina = readStream(in);
            urlC.disconnect();
            return pagina;

        } catch (Exception ex) {
            Log.d("Erro", "Erro aqui", ex);
            return "";
        }


    }


    private static String readStream(InputStream in) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while (i != -1) {
                bo.write(i);
                i = in.read();
            }
            return bo.toString();
        } catch (Exception ex) {
            return "";
        }
    }

}
