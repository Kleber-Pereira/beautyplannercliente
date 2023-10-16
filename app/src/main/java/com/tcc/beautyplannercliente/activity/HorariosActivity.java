package com.tcc.beautyplannercliente.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.beautyplannercliente.R;
import com.tcc.beautyplannercliente.adapter.AdapterListView;
import com.tcc.beautyplannercliente.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HorariosActivity extends AppCompatActivity implements AdapterListView.ClickItemListView {


    private ListView listView;
    private AdapterListView adapterListView;
    private List<String> horarios= new ArrayList<String>();

    private List<String> horarios_Temp= new ArrayList<String>();


    private ArrayList<String> data = new ArrayList<String>();

    private String funcoesservicoNome;
    private String funcoesfuncionarioNome;




    //firebase
    private FirebaseDatabase database;

    private DatabaseReference referenceBuscarHorario;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);




        listView = (ListView)findViewById(R.id.listView);


        database = FirebaseDatabase.getInstance();
        data = getIntent().getStringArrayListExtra("data");
        funcoesservicoNome = getIntent().getStringExtra("servicoservico");
        funcoesfuncionarioNome = getIntent().getStringExtra("funcionarioNome");



        configurarListView();

        carregarHorarioFuncionamento();




    }


    //-------------------------------------CONFIGURAR LISTVIEW-------------------------------------------------

    private void configurarListView(){

        adapterListView = new AdapterListView(this,horarios,this);
        listView.setAdapter(adapterListView);

    }




    //-------------------------------------CARREGAR HORARIOS EMPRESA-------------------------------------------------


    private void carregarHorarioFuncionamento(){



        DatabaseReference reference = database.getReference().
                child("BD").child("Calendario").child("HorariosFuncionamento");



        reference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){


                        String horario = snapshot.getValue(String.class);

                        horarios.add(horario);

                        horarios_Temp.add(horario);


                    }

                    adapterListView.notifyDataSetChanged();

                    buscarHorariosReservados();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    //-------------------------------------BUSCA HORARIOS AGENDADOS-------------------------------------------------

    //ESSE METODO É CHAMADO SOMENTE DEPOIS QUE O METODO ACIMA FOR EXECUTADO

    private void buscarHorariosReservados(){


        /*referenceBuscarHorario = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0));*/

        referenceBuscarHorario = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0)).
                child(funcoesservicoNome).child(funcoesfuncionarioNome);



        if (childEventListener == null){


            childEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                    String chave = dataSnapshot.getKey();  // le o nome da pasta

                    int index = horarios.indexOf(chave);



                    String horario = chave + " - Reservado";



                    horarios.set(index,horario);


                    adapterListView.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {



                    String chave = dataSnapshot.getKey();  // le o nome da pasta


                    String horario = chave + " - Reservado";


                    int index = horarios.indexOf(horario);



                    horarios.set(index,chave);


                    adapterListView.notifyDataSetChanged();


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            referenceBuscarHorario.addChildEventListener(childEventListener);

        }

    }





    //-------------------------------------CLICK ITEM DA LISTA------------------------------------------------

    @Override
    public void clickItem(String horario, int posicao) {


        if (Util.statusInternet_MoWi(getBaseContext())) {


            consultarHorarioSelecionadoBanco(horario, posicao);

        }else{

            Toast.makeText(getBaseContext(),"Erro - Sem conexão com a Internet",Toast.LENGTH_LONG).show();


        }

    }

/*
    private void consultarHorarioSelecionado(String horario, int posicao){


        if (horario.contains("Reservado")){

            Toast.makeText(getBaseContext(),"Já existe um agendamento para esse horário",Toast.LENGTH_LONG).show();

        }else{


            Toast.makeText(getBaseContext(),"Agendamento Disponivel",Toast.LENGTH_LONG).show();


            //chamar a nossa proxima activity.

        }

    }
    */



    private void consultarHorarioSelecionadoBanco(final String horario, final int posicao){



        /*DatabaseReference reference = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(horarios_Temp.get(posicao));*/

        DatabaseReference reference = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(funcoesservicoNome).
                child(funcoesfuncionarioNome).child(horarios_Temp.get(posicao));

        /*referencia
        referenceBuscarHorario = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0)).
                child(funcoesservicoNome).child(funcoesfuncionarioNome);*/

        reference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){


                    String emailRecuperadoBancoDados = dataSnapshot.child("email").getValue(String.class);

                    String emailDispositivoUsuario = obterEmail();


                    if (emailRecuperadoBancoDados.equals(emailDispositivoUsuario)){

                        Toast.makeText(getBaseContext(),"O e-mail é Igual você pode alterar ou Remover os dados",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(),AlterarRemoverServicoActivity.class);
                        String horarioSelecionado = horarios_Temp.get(posicao);

                        data.add(3,horarioSelecionado);

                        intent.putExtra("data",data);
                        intent.putExtra("servicoservico",funcoesservicoNome);
                        intent.putExtra("funcionarioNome",funcoesfuncionarioNome);



                        startActivity(intent);

                    }else{


                        Toast.makeText(getBaseContext(),"Não foi você quem fez o agendamento",Toast.LENGTH_LONG).show();


                    }


                }else{

                  // Toast.makeText(getBaseContext(),"Agendamento Disponivel",Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(getBaseContext(), AgendamentoServicoActivity.class);

                    //  data.add(dia);// posicao 0
                    //  data.add(mes);// posicao 1
                    //  data.add(ano);// posicao 2
                    //  data.add(horario);// posicao 3

                    data.add(3,horario);


                    intent.putExtra("data",data);
                    intent.putExtra("servicoservico",funcoesservicoNome);
                    intent.putExtra("funcionarioNome",funcoesfuncionarioNome);


                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    //----------------------------------OBTER NUMERO EMAIL-------------------------------------------------


    private String obterEmail(){


        AccountManager accountManager = AccountManager.get(this);

        Account[] accounts = accountManager.getAccounts();


        for (Account account: accounts){

            String email = account.name; // email = jonejhgajhgf@gmail.com

            if(email.contains("@")){

                return email;

            }
        }


        return "";

    }








    //-------------------------------------CLICO DE VIDA ACTIVITY------------------------------------------------



    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (childEventListener == null){


            referenceBuscarHorario.removeEventListener(childEventListener);

          //  childEventListener = null;

        }


    }
}
