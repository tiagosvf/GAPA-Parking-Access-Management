package com.example.a6100331.softinsa_gestao;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Pagina_Eventos extends AppCompatActivity {

    String resultado;
    String[] listaIds;
    public static final int MENSAGEM = 1;
    TextView tvi;
    ProgressBar pg;
    ListView lv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean refreshing;
    private static Pagina_Eventos myapp = null;

    public static Context context() {


        return myapp.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina__eventos);

        myapp = this;

        getSupportActionBar().setTitle("Eventos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvi = (TextView) this.findViewById(R.id.tvi_eventos);
        pg = (ProgressBar) this.findViewById(R.id.pg_eventos);

        lv = (ListView) this.findViewById(R.id.lv_eventos);
        lv.setVisibility(View.INVISIBLE);


        new Thread() {
            public void run() {
                resultado = pedidosHTTP.listar("/mobile_ListaEventos.aspx?id="+  preferencias.recebeId());
                handler.sendEmptyMessage(MENSAGEM);
            }
        }.start();

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh_layout);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                onRefreshx();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intencao = new Intent(Pagina_Eventos.context(), Pagina_Ver_Evento.class);
                intencao.putExtra("posicao", position);
                startActivity(intencao);
            }
        });



    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                this.finish();
                return true;


        }
    }

    public void onRefreshx() {

        Log.d("Vamos ver", "se chega aqui ");
        lv.setVisibility(View.INVISIBLE);
        pg.setVisibility(View.VISIBLE);
        tvi.setVisibility(View.VISIBLE);
        tvi.setText("A obter dados...");
        Lista_Eventos.load();

        new Thread() {
            public void run() {
                resultado = pedidosHTTP.listar("/mobile_ListaEventos.aspx?id=" + preferencias.recebeId());
                handler.sendEmptyMessage(MENSAGEM);
                Log.d("resultado:", "" + resultado);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MENSAGEM:
                    try {
                        if (refreshing == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.destroyDrawingCache();
                            mSwipeRefreshLayout.clearAnimation();
                            refreshing = false;
                        }

                        tvi.setText(resultado);
                        pg.setVisibility(View.GONE);
                        if (resultado == null || resultado == "") {
                            tvi.setText("Ocorreu um erro ao obter dados");

                            break;
                        }

                        if(resultado=="[]"){
                            tvi.setText("Não existem eventos");
                            break;
                        }


                        JSONArray jsonarray;
                        JSONObject jsonobject;
                        Log.d("Resultado:", resultado);
                        resultado = "{\"lista\":" + resultado + "}";
                        jsonobject = new JSONObject(resultado);
                        jsonarray = jsonobject.getJSONArray("lista");
                        Lista_Eventos.load();

                        Log.d("Tamanho array:", "" + jsonarray.length());
                        listaIds = new String[jsonarray.length()];
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            int id = jsonobject.getInt("id");
                            String tipo = jsonobject.getString("tipo");
                            String nome_utilizador = jsonobject.getString("nome_utilizador");
                            int id_utilizador = jsonobject.getInt("id_utilizador");
                            String data_hora = jsonobject.getString("data_hora");
                            String descricao = jsonobject.getString("descricao");
                            String portao = jsonobject.getString("nome_portao");

                            Log.d("Teste 1:", "" + descricao);
                            Evento evento = new Evento(id, tipo, nome_utilizador,id_utilizador,data_hora,descricao,portao);

                            Lista_Eventos.adicionar(evento);
                            listaIds[i] = String.valueOf(id);

                        }
                        lv.setVisibility(View.VISIBLE);
                        ItemAdapter itemAdapter = new ItemAdapter(Pagina_Eventos.context(), R.layout.linha, listaIds);
                        Log.d("Lista de eventos:", "" + Lista_Eventos.lista.size());
                        lv.setAdapter(null);
                        lv.post(new Runnable() {
                            @Override
                            public void run() {
                                ItemAdapter itemAdapter = new ItemAdapter(Pagina_Eventos.context(), R.layout.linha, listaIds);
                                lv.setAdapter(itemAdapter);
                            }
                        });
                        Log.d("Numero de items lista:", "" + itemAdapter.getCount());


                        tvi.setVisibility(View.INVISIBLE);
                        itemAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.d("Erro", "Erro aqui", ex);
                    }

                    break;
            }
        }
    };
}
