package com.example.a6100331.softinsa_gestao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Pagina_Login extends AppCompatActivity {

    ProgressDialog rodinha;
    String resultado;
    public static final int LOGIN = 2;
    public static final int CONECTIVIDADE = 3;
    String url;
    EditText txt_username;
    EditText txt_password;
    private static Pagina_Login myapp = null;
    ImageView iv_logo;

    public static Context context() {


        return myapp.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.ThemeLogin);
        super.onCreate(savedInstanceState);

        myapp = this;

        if (preferencias.recebeId() != -1) {
            Intent intecao = new Intent(this, Pagina_Principal.class);
            startActivity(intecao);
            finish();
        }

        setContentView(R.layout.activity_pagina__login);

        iv_logo=(ImageView) this.findViewById(R.id.iv_logo);
        iv_logo.setImageResource(R.drawable.logotipo_softinsa_2016);


        getSupportActionBar().setTitle("");

        if (android.os.Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().setElevation(0);

        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);


        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v,
                                      boolean hasFocus) {
                testarUsername();
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.ab_login, menu);
        return true;


        //return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intencao;
        switch (item.getItemId()) {
            case R.id.acao_definicoes:

                final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);

                final EditText url = new EditText(this);
                url.setHint("URL");
                url.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                if(TextUtils.isEmpty(preferencias.recebeLink()))
                    url.setText(preferencias.recebeLink());
                else
                    url.setText(preferencias.recebeLink().substring(7,preferencias.recebeLink().length()));

                //http://stackoverflow.com/questions/9345735/resizing-edittext-inside-of-an-alertdialog
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                float dpi = this.getResources().getDisplayMetrics().density; //http://stackoverflow.com/questions/33074313/getting-default-padding-for-alertdialog
                params.setMargins((int) (19 * dpi), 0, (int) (14 * dpi), 0);


                layout.addView(url, params);


                myAlert.setView(layout);


                myAlert.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });


                myAlert.setTitle("Definir o URL do servidor");

                myAlert.setMessage("Insira o URL ou IP do seu servidor. (www.exemplo.com OU 199.199.199.199)").create();
                final AlertDialog dialog = myAlert.create();
                dialog.show();


                //http://stackoverflow.com/questions/6142308/android-dialog-keep-dialog-open-when-button-is-pressed
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;
                        if (TextUtils.isEmpty(url.getText().toString())) {
                            url.setError("Deve inserir um URL");
                        } else if (url.getText().toString().contains("http")) {
                            url.setError("Não é preciso definir o protocolo");
                        } else if (!url.getText().toString().contains(".")) {
                            url.setError("Deve inserir um URL válido");
                        } else {
                            preferencias.guardaLink(url.getText().toString());
                            wantToCloseDialog = true;
                        }
                        if (wantToCloseDialog)
                            dialog.dismiss();

                    }
                });
                return true;


            default:
                this.finish();
                return true;
            //  return super.onOptionsItemSelected(item);

        }
    }

    public boolean testarUsername() {

        boolean ok = true;
        if (TextUtils.isEmpty(txt_username.getText())) {
            txt_username.setError("Tem que introduzir um username");
            ok = false;
        } /*else if (txt_username.getText().toString().length()<5) {
            txt_username.setError("Username muito curto");
            ok = false;
        }*/
        return ok;
    }




    public void onClickEntrar(View view) {


        boolean ok = true;

        if (TextUtils.isEmpty(txt_password.getText())) {
            txt_password.setError("Tem que introduzir a sua palavra-passe");
            ok = false;
        }

        if (testarUsername() == false)
            ok = false;
        if(TextUtils.isEmpty(preferencias.recebeLink())){
            AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Login.this).create();
        alertDialog.setTitle("Erro");
        alertDialog.setMessage("Deve inserir o URL do servidor nas definições.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });
        alertDialog.show();
        ok=false;}

        if (ok == false)
            return;
        else {


            url = "/mobile_teste.aspx";


            rodinha = ProgressDialog.show(Pagina_Login.this, "A estabelecer ligação", "Por favor aguarde...");
            new Thread() {
                public void run() {
                    resultado = pedidosHTTP.listar(url);

                    handler.sendEmptyMessage(CONECTIVIDADE);

                }
            }.start();


        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:
                    rodinha.dismiss();
                    try {
                        if (resultado == null || resultado == "" || resultado == "[]" || resultado.contains("-")) {

                            Log.d("Resultado_login:", resultado);
                            txt_username.setError("O login falhou");

                            // rodinha.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Login.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("O login falhou. Verifique as suas credencias.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();


                                        }
                                    });
                            alertDialog.show();


                            Log.d("teste: ", "rodinha");
                        } else {

                            Log.d("Resultado Login:", resultado);
                            Log.d("teste: ", "else");
                            preferencias.guardaId(Integer.valueOf(resultado.substring(0, resultado.indexOf(','))), resultado.substring(resultado.indexOf(',') + 1, resultado.indexOf('/'))); //TODO:  O DAVI TEM QUE ME ENVIAR O ID,NOME
                            preferencias.guardaPW(resultado.substring(resultado.indexOf('/') +1,resultado.length()));
                            Log.d("PW:",preferencias.recebePW());
                            Intent intecao = new Intent(Pagina_Login.context(), Pagina_Principal.class);
                            startActivity(intecao);
finish();
                        }
                    } catch (Exception ex) {
                        Log.d("Erro", "Erro aqui", ex);
                    }

                    break;

                case CONECTIVIDADE:
                    try {
                        Log.d("Resultado:", resultado);
                        if (!TextUtils.equals(resultado, "+1")) {
                            Log.d("Falhou:", resultado);
                            //  rodinha.dismiss();
                            rodinha.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Pagina_Login.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("O login falhou. Não foi possível estabelecer ligação ao servidor.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                        }
                                    });
                            alertDialog.show();

                        } else {

                            try {
                                url = "/mobile_login.aspx?username=" + URLEncoder.encode(txt_username.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(txt_password.getText().toString(), "UTF-8");
                            } catch (Exception e) {
                                url = "/mobile_login.aspx?username=" + txt_username.getText() + "&password=" + txt_password.getText();
                            }


                            new Thread() {
                                public void run() {
                                    resultado = pedidosHTTP.listar(url);
                                    handler.sendEmptyMessage(LOGIN);
                                }
                            }.start();

                        }
                    } catch (Exception ex) {
                        Log.d("Erro", "Erro aqui", ex);
                    }

                    break;
            }
        }
    };


}
