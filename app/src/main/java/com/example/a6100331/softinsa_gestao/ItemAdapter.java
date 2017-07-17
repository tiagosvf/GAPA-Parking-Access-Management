package com.example.a6100331.softinsa_gestao;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Tiago on 04/05/2017.
 */

public class ItemAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int rowResourceId;
    private final String[] lista_ids;

    public ItemAdapter(Context context, int textViewResourceId, String[] lista_ids) {
        super(context, textViewResourceId);

        this.context = context;
        this.rowResourceId = textViewResourceId;
        //  this.lista_obj=lista_obj;
        this.lista_ids = lista_ids;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d("Chegou ao getview?", "Chegou");

        View rowView = inflater.inflate(rowResourceId, parent, false);
        ImageView iv_evento = (ImageView) rowView.findViewById(R.id.iv_evento);
        TextView lbl_tipo = (TextView) rowView.findViewById(R.id.lbl_tipo);
        TextView lbl_data = (TextView) rowView.findViewById(R.id.lbl_data_hora);
        TextView lbl_user = (TextView) rowView.findViewById(R.id.lbl_user);
        lbl_tipo.setTextColor(Color.GRAY);
        lbl_data.setTextColor(Color.GRAY);
        lbl_user.setTextColor(Color.GRAY);

        Log.d("Chegou ao getview?", "Chegou");
        lbl_tipo.setText(Lista_Eventos.get(position).tipo);
        lbl_data.setText(Lista_Eventos.get(position).data_hora.substring(5,10) + " " + Lista_Eventos.get(position).data_hora.substring(11,16));
        lbl_user.setText(Lista_Eventos.get(position).nome_utilizador);




        try {

            Picasso.with(Pagina_Login.context()).load(Link.getLink()+"/Imagens_eventos_mini/i" + Lista_Eventos.get(position).id + ".jpg").into(iv_evento);
            if (iv_evento.getDrawable() == null)
                throw new Exception("Não encontrou imagem");
        } catch (Exception ex) {
            iv_evento.setImageResource(R.drawable.icon_sem_imagem_branco);
        }
        return rowView;
    }



    @Override
    public int getCount() {
        return Lista_Eventos.lista.size();
    }


}
