package com.example.a6100331.softinsa_gestao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Pagina_Ver_Evento extends AppCompatActivity {

    int posicao;
    Bundle extras;
    TextView lbl_tipo;
    TextView lbl_portao;
    TextView lbl_descricao;
    TextView lbl_data;
    TextView lbl_utilizador;
    ImageView iv_evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);


        lbl_tipo = (TextView) this.findViewById(R.id.lbl_tipo);
        lbl_portao = (TextView) this.findViewById(R.id.lbl_portao);
        lbl_descricao = (TextView) this.findViewById(R.id.lbl_descricao);
        lbl_data = (TextView) this.findViewById(R.id.lbl_data);
        lbl_utilizador = (TextView) this.findViewById(R.id.lbl_utilizador);


        iv_evento = (ImageView) this.findViewById(R.id.iv_evento);


        getSupportActionBar().setTitle("Detalhes do evento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        posicao = extras.getInt("posicao");

        preencher();

    }

    public void preencher() {
        Log.d("chega aqui?", "preencher perdido linha84");
       try {
            Picasso.with(Pagina_Eventos.context()).load(Link.getLink()+"/Imagens_eventos_mini/i" + Lista_Eventos.get(posicao).id + ".jpg").into(iv_evento);
            if (iv_evento.getDrawable() == null)
                throw new Exception("Lele");
        } catch (Exception ex) {
            iv_evento.setVisibility(View.GONE);
        }



        Log.d("teste DC:", Lista_Eventos.get(posicao).portao);
        lbl_utilizador.setText(Lista_Eventos.get(posicao).nome_utilizador);
        lbl_portao.setText(Lista_Eventos.get(posicao).portao);
        lbl_tipo.setText(Lista_Eventos.get(posicao).tipo);
        lbl_descricao.setText(Lista_Eventos.get(posicao).descricao);
        String data = Lista_Eventos.get(posicao).data_hora;
        data = data.substring(0, 10)+"  "+ data.substring(11,16) ;
        lbl_data.setText(data);



    }





    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                this.finish();
                return true;


        }
    }
}
