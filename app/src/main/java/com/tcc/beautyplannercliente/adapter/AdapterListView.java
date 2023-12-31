package com.tcc.beautyplannercliente.adapter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.tcc.beautyplannercliente.R;

import java.util.List;

public class AdapterListView extends BaseAdapter {


    private Activity activity;
    private List<String> horarios;
    private ClickItemListView clickItemListView;


    public AdapterListView(Activity activity, List<String> horarios, ClickItemListView clickItemListView){

        this.activity = activity;
        this.horarios = horarios;
        this.clickItemListView = clickItemListView;

    }


    @Override
    public int getCount() {
        return horarios.size();
    }

    @Override
    public Object getItem(int posicao) {

        return horarios.get(posicao);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int posicao, View ConvertView, ViewGroup parent) {


        View view = activity.getLayoutInflater().inflate(R.layout.listview_item,parent,false);

        TextView textView = (TextView)view.findViewById(R.id.textView_ListView_Item);
        CardView cardView = (CardView)view.findViewById(R.id.cardView_ListView_Item);

        textView.setText(horarios.get(posicao));


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                clickItemListView.clickItem(horarios.get(posicao),posicao);


            }
        });




        return view;
    }




    public interface ClickItemListView {



        void clickItem(String horario, int posicao);


    }

}
