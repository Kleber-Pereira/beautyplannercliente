package com.tcc.beautyplannercliente.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tcc.beautyplannercliente.R;
import com.tcc.beautyplannercliente.agendamento.FuncoesBuscarServicoActivity;
import com.tcc.beautyplannercliente.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class FragmentCalendario extends Fragment implements CalendarView.OnDateChangeListener {

    private CalendarView calendarView;
    private int dia_Atual;
    private int mes_Atual;
    private int ano_Atual;



    public FragmentCalendario() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView_Calendario);

        obterDataAtual();
        configurarCalendario();

        calendarView.setOnDateChangeListener(this);


        return view;
    }


    //---------------------------------------- OBTER DATA ATUAL ----------------------------------------



    private void obterDataAtual(){

        long dataLong = calendarView.getDate(); //123456465465213

        Locale locale = new Locale("pt","BR");

        SimpleDateFormat dia = new SimpleDateFormat("dd",locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM",locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy",locale);




        dia_Atual = Integer.parseInt(dia.format(dataLong));

        mes_Atual = Integer.parseInt(mes.format(dataLong));

        ano_Atual = Integer.parseInt(ano.format(dataLong));



        //   Toast.makeText(getContext(),
        //       "Dia: "+dia_Atual+ "\nMes: "+ mes_Atual + "\nAno: "+ano_Atual,Toast.LENGTH_LONG).show();

    }





    //---------------------------------------- CONFIGURAR CALENDARIO ----------------------------------------


    private void configurarCalendario(){


        Calendar dataMinima = configurarDataMinima();
        Calendar dataMaxima = configurarDataMaxima();


        calendarView.setMinDate(dataMinima.getTimeInMillis());
        calendarView.setMaxDate(dataMaxima.getTimeInMillis());

    }



    private Calendar configurarDataMinima(){


        Calendar dataMinima = Calendar.getInstance();
        int diaInicioCalendario = 1;

        dataMinima.set(ano_Atual,mes_Atual-1,diaInicioCalendario);


        return dataMinima;


    }


    private Calendar configurarDataMaxima(){

        Calendar dataMaxima = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();


        int diaFinalCalendario = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        if (dia_Atual == diaFinalCalendario){

            dataMaxima.set(ano_Atual,mes_Atual,15);

        }else{


            dataMaxima.set(ano_Atual,mes_Atual-1,diaFinalCalendario);
        }


        return  dataMaxima;

    }




    //---------------------------------------- CLICK CALENDARIO ----------------------------------------

    @Override
    public void onSelectedDayChange(CalendarView calendarView,
                                    int anoSelecionado, int mesSelecionado, int diaSelecionado) {


        int mes = mesSelecionado + 1;



        dataSelecionada(diaSelecionado,mes,anoSelecionado);

    }


    private void dataSelecionada(int diaSelecionado,int mesSelecionado,int anoSelecionado){



        Locale locale = new Locale("pt","BR");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",locale);

        Calendar data = Calendar.getInstance();


        try {
            data.setTime(simpleDateFormat.parse(diaSelecionado+"/"+mesSelecionado+"/"+anoSelecionado));

             boolean disponivelAgendamento ;


            if (mesSelecionado != mes_Atual){
                disponivelAgendamento = true;

            }else{

                disponivelAgendamento = agendaDisponivel(data, diaSelecionado);

            }
            if (disponivelAgendamento){


                if (Util.statusInternet_MoWi(getContext())){

                    String dia = String.valueOf(diaSelecionado);
                    String mes = String.valueOf(mesSelecionado);
                    String ano = String.valueOf(anoSelecionado);

                    ArrayList<String> dataList = new ArrayList<String>();

                    dataList.add(dia);// posicao 0
                    dataList.add(mes);// posicao 1
                    dataList.add(ano);// posicao 2

                    //Intent intent = new Intent(getContext(), HorariosActivity.class);
                    Intent intent = new Intent(getContext(), FuncoesBuscarServicoActivity.class);
                    intent.putExtra("data",dataList);

                    startActivity(intent);

                }else{

                    Toast.makeText(getContext(),"Erro - Sem conexão com a internet",Toast.LENGTH_LONG).show();
                }



            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private boolean agendaDisponivel(Calendar data, int diaSelecionado){


        Calendar calendar = Calendar.getInstance();

        int diaFinalCalendario = calendar.getActualMaximum(mes_Atual-1);

        if (diaFinalCalendario == dia_Atual){

            Toast.makeText(getContext(), "Agendamento disponivel do dia 1° para frente",Toast.LENGTH_LONG).show();

            return false;

        }

        else if(diaSelecionado <= dia_Atual){

            Toast.makeText(getContext(),
                    "Agendamento disponivel do dia "+(dia_Atual+1)+ " para frente.",Toast.LENGTH_LONG).show();

            return false;

        }else{


            return true;

        }

    }




}