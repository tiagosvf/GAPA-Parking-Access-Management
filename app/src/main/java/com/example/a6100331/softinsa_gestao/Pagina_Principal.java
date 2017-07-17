package com.example.a6100331.softinsa_gestao;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class Pagina_Principal extends AppCompatActivity {

    public static final int MUDAR_PORTAO = 1;
    public static final int RECEBER_PORTOES = 2;
    String resultado;
    String[] listaIds;
    TextView tvi;
    ProgressBar pg;
    ProgressDialog rodinha;
    LinearLayout layout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean refreshing;

   // private TextView txt_resultado_fala;
    private ImageButton btn_fala;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    //TODO: COLOCAR OS BOTÕES DINAMICAMENTE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina__principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GAPA");
        if (android.os.Build.VERSION.SDK_INT >= 23)
        toolbar.setBackgroundColor(getColor(R.color.azulEscuro));
        else
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        toolbar.setLogo(R.drawable.logotipo_softinsa_2016_negativo4);


        setSupportActionBar(toolbar);

        //txt_resultado_fala = (TextView) findViewById(R.id.txt_resultado_fala);
        btn_fala = (ImageButton) findViewById(R.id.btn_fala);


        tvi = (TextView) this.findViewById(R.id.tvi_portoes);
        pg = (ProgressBar) this.findViewById(R.id.pg_portoes);
        layout = (LinearLayout) this.findViewById(R.id.linear_portoes);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        btn_fala.setElevation(60);

        layout.setVisibility(View.GONE);
        btn_fala.setVisibility(View.GONE);
       // txt_resultado_fala.setVisibility(View.GONE);

        new Thread() {
            public void run() {
                resultado = pedidosHTTP.listar("/mobile_portoes.aspx?id_utilizador=" +preferencias.recebeId());
                handler.sendEmptyMessage(RECEBER_PORTOES);
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


        btn_fala.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-PT");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                 //   txt_resultado_fala.setText(result.get(0));

                    for (int i=0; i<Lista_Portoes.lista.size(); i++)
                    {
                      //  if(TextUtils.equals("abrir "+ Lista_Portoes.get(i).nome.toLowerCase(), result.get(0)) || TextUtils.equals("fechar "+ Lista_Portoes.get(i).nome.toLowerCase(), result.get(0)))
                          if(result.get(0).contains("abrir "+ Lista_Portoes.get(i).nome.toLowerCase()) || result.get(0).contains("fechar "+ Lista_Portoes.get(i).nome.toLowerCase()))

                        {
                            final String url = "/mobile_AbrirFechar.aspx?id=" + Lista_Portoes.get(i).id + "&utilizador=" + preferencias.recebeId() +"&pw=" + preferencias.recebePW();
//                                    rodinha = ProgressDialog.show(Pagina_Login.context(), "A estabelecer ligação", "Por favor aguarde...");
                            new Thread() {
                                public void run() {
                                    resultado = pedidosHTTP.listar(url);
                                    handler.sendEmptyMessage(MUDAR_PORTAO);
                                }
                            }.start();
                        }
                        //else if(TextUtils.equals("ajuda", result.get(0)) ){
                        else if(result.get(0).contains("ajuda")){
                            AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Principal.this).create();
                            alertDialog.setTitle("Comandos de voz");
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                alertDialog.setMessage(Html.fromHtml("<b>"+"abrir [nome do portão]"+"</b>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>" + "<b>"+"fechar [nome do portão]"+"</b><br/>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>"  + "<b>"+"sair"+"</b>" + " - sai da aplicação "+"<br/><br/>" + "<b>"+"ajuda"+"</b>" + " - abre esta página de ajuda", Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                alertDialog.setMessage(Html.fromHtml("<b>"+"abrir [nome do portão]"+"</b>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>" + "<b>"+"fechar [nome do portão]"+"</b><br/>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>"  + "<b>"+"sair"+"</b>" + " - sai da aplicação "+"<br/><br/>" + "<b>"+"ajuda"+"</b>" + " - abre esta página de ajuda \n"));
                            }

                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        //else if(TextUtils.equals("sair", result.get(0))){
                          else if(result.get(0).contains("sair")){
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }


                }
                break;
            }

        }
    }

    public void onRefreshx() {
        pg.setVisibility(View.VISIBLE);
        tvi.setVisibility(View.VISIBLE);
        tvi.setText("A obter dados...");
        layout.setVisibility(View.GONE);
        btn_fala.setVisibility(View.GONE);

     //   txt_resultado_fala.setVisibility(View.GONE);

        new Thread() {
            public void run() {
                resultado = pedidosHTTP.listar("/mobile_portoes.aspx?id_utilizador=" +preferencias.recebeId() );
                handler.sendEmptyMessage(RECEBER_PORTOES);
            }
        }.start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {


       /* if (TextUtils.equals("Administrador", preferencias.recebeTipo())) {*/
            getMenuInflater().inflate(R.menu.ab_principal_admin, menu);
            return true;

       /* } else {
            getMenuInflater().inflate(R.menu.ab_principal_user, menu);
            return true;
        }*/

        //return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intencao;
        switch (item.getItemId()) {
            case R.id.acao_eventos:

                intencao = new Intent(Pagina_Login.context(), Pagina_Eventos.class);
                startActivity(intencao);

                return true;


            case R.id.acao_logout:
                preferencias.removeId();

                intencao = new Intent(Pagina_Login.context(), Pagina_Login.class);
                startActivity(intencao);
                finish();
                return true;

            case R.id.acao_ajuda:
                AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Principal.this).create();
                alertDialog.setTitle("Comandos de voz");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    alertDialog.setMessage(Html.fromHtml("<b>"+"abrir [nome do portão]"+"</b>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>" + "<b>"+"fechar [nome do portão]"+"</b><br/>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>"  + "<b>"+"sair"+"</b>" + " - sai da aplicação "+"<br/><br/>" + "<b>"+"ajuda"+"</b>" + " - abre esta página de ajuda", Html.FROM_HTML_MODE_LEGACY));
                } else {
                    alertDialog.setMessage(Html.fromHtml("<b>"+"abrir [nome do portão]"+"</b>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>" + "<b>"+"fechar [nome do portão]"+"</b><br/>" + " - abre/fecha o portão cujo nome foi dito " +"<br/><br/>"  + "<b>"+"sair"+"</b>" + " - sai da aplicação "+"<br/><br/>" + "<b>"+"ajuda"+"</b>" + " - abre esta página de ajuda \n"));
                }

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            default:
                this.finish();
                return true;
            //  return super.onOptionsItemSelected(item);

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEBER_PORTOES:
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
                            tvi.setText("Ocorreu um erro ao obter dados.");

                            break;
                        } else if (resultado == "[]" || resultado.contains("-"))  {
                            tvi.setText("Não existem portões.");
                            break;
                        }


                        JSONArray jsonarray;
                        JSONObject jsonobject;
                        Log.d("Resultado:", resultado);

                        resultado = "{\"lista\":" + resultado + "}";
                        jsonobject = new JSONObject(resultado);
                        jsonarray = jsonobject.getJSONArray("lista");

                        Lista_Portoes.load();

                        // lista_objetos.limpar();
                        Log.d("Tamanho array:", "" + jsonarray.length());
                        listaIds = new String[jsonarray.length()];
                        layout.removeAllViews();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            final int id = jsonobject.getInt("id");


                            String nome = jsonobject.getString("nome");

                            Portao portao = new Portao(id,nome);

                            Lista_Portoes.adicionar(portao);


                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            if(i!=0)
                            lp.setMargins(5, 20, 5, 36);
                            else
                                lp.setMargins(5,50,5,36);


                            Button btn_portao = new Button(Pagina_Login.context());
                            btn_portao.setLayoutParams(lp);

                            Log.d("principal:", "chega aqui4");
                            btn_portao.setPadding(2, 2, 2, 2);

                            btn_portao.setId(id);
                            btn_portao.setText(nome.toUpperCase());
                            btn_portao.setTypeface(null, Typeface.BOLD);

                            try {
                                if (android.os.Build.VERSION.SDK_INT > 22){
                                    btn_portao.setBackgroundColor(getColor(R.color.cinzentoFundo));
                                    btn_portao.setTextColor(getColor(R.color.cinzentoBotao));
                                    btn_portao.setElevation(8);}
                            }
                            catch(Exception e){
                                btn_portao.setBackgroundColor(getResources().getColor(R.color.cinzentoFundo));
                                btn_portao.setTextColor(getResources().getColor(R.color.cinzentoBotao));

                            }


                            btn_portao.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    final String url = "/mobile_AbrirFechar.aspx?id=" + id + "&utilizador=" + preferencias.recebeId()+"&pw=" + preferencias.recebePW();
//                                    rodinha = ProgressDialog.show(Pagina_Login.context(), "A estabelecer ligação", "Por favor aguarde...");
                                    new Thread() {
                                        public void run() {
                                            resultado = pedidosHTTP.listar(url);
                                            handler.sendEmptyMessage(MUDAR_PORTAO);
                                        }
                                    }.start();

                                }
                            });

                            Log.d("perfil:", "chega aqui2");
                            layout.addView(btn_portao);


                           /*
                            Log.d("Teste 1:", "" + localidade);
                            objeto obj = new objeto(id, dc, descricao, categoria, data_publicado, id_utilizador, coordenadas, distrito, localidade, "Perdido",nome,email,contacto);

                            lista_objetos.adicionar(obj);
                            listaIds[i] = String.valueOf(id);*/

                        }


                        pg.setVisibility(View.GONE);
                        tvi.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        btn_fala.setVisibility(View.VISIBLE);
                    //    txt_resultado_fala.setVisibility(View.VISIBLE);

                        tvi.setVisibility(View.INVISIBLE);

                    } catch (Exception ex) {
                        Log.d("Erro", "Erro aqui", ex);
                    }

                    break;

                case MUDAR_PORTAO:
                    try {
//                        rodinha.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Principal.this).create();
                        if(resultado.contains("2"))
                        {
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Só pode ter sessão iniciada em um dispositivo de cada vez.");
                        }
                        else if (resultado == null || resultado == "" || resultado == "[]" || resultado.contains("1")) {
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Não foi possível aceder ao portão.");

                        }
                       else {
                            alertDialog.setTitle("Sucesso");
                            alertDialog.setMessage("O estado do portão será alterado.");
                        }

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        if(resultado.contains("-2")){
                                            preferencias.removeId();

                                            Intent intencao;
                                            intencao = new Intent(Pagina_Login.context(), Pagina_Login.class);
                                            startActivity(intencao);
                                            finish();
                                        }
                                    }
                                });
                        alertDialog.show();
                    } catch (Exception ex) {
                        Log.d("Erro", "Erro aqui", ex);
                    }


                    break;
            }
        }
    };


}
