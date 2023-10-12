package com.tcc.beautyplannercliente.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.beautyplannercliente.R;
import com.tcc.beautyplannercliente.modelo.Agendamento;
import com.tcc.beautyplannercliente.util.DialogProgress;
import com.tcc.beautyplannercliente.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlterarRemoverServicoActivity extends AppCompatActivity implements View.OnClickListener  {



    private EditText editText_Nome;
    private EditText editText_NumeroContato;
    private CheckBox checkBox_WhatsApp;
    private EditText editText_Email;
    private CheckBox checkBox_Barba;
    private CheckBox checkBox_Cabelo;
    private CardView cardView_Alterar;
    private CardView cardView_Remover;



    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    private Agendamento agendamento;





    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_remover_servico);



        data = getIntent().getStringArrayListExtra("data");

        database = FirebaseDatabase.getInstance();


        editText_Nome = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Nome);
        editText_NumeroContato = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Numero);
        checkBox_WhatsApp = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_WhatsApp);
        editText_Email = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Email);
        checkBox_Barba = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_barba);
        checkBox_Cabelo = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_Cabelo);
        cardView_Alterar = (CardView)findViewById(R.id.cardView_AlterarRemeverServico_Alterar);
        cardView_Remover = (CardView)findViewById(R.id.cardView_AlterarRemeverServico_Remover);




        cardView_Alterar.setOnClickListener(this);
        cardView_Remover.setOnClickListener(this);


        obterDadosAgendamento();

    }


    //----------------------------------ACAO DE CLICK--------------------------------------------------

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case R.id.cardView_AlterarRemeverServico_Alterar:

                validarCampos();

                break;

            case R.id.cardView_AlterarRemeverServico_Remover:

                removerAgendamento();


                break;

        }

    }





    //----------------------------------OBTER DADOS DO SERVICO--------------------------------------------------


    private void obterDadosAgendamento(){




        reference = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(data.get(3));


        if( valueEventListener == null ){

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()){


                        Agendamento agendamentoCliente = dataSnapshot.getValue(Agendamento.class);


                        agendamento = agendamentoCliente;


                        atualizarDados(agendamentoCliente);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            reference.addValueEventListener(valueEventListener);

        }

    }





    private void atualizarDados(Agendamento agendamento){


        editText_Nome.setText(agendamento.getNome());
        editText_NumeroContato.setText(agendamento.getContato());
        editText_Email.setText(agendamento.getEmail());
        checkBox_WhatsApp.setChecked(agendamento.isWhatsApp());
        checkBox_Cabelo.setChecked(agendamento.isCabelo());
        checkBox_Barba.setChecked(agendamento.isBarba());

    }











    //----------------------------------AGENDAR NO FIREBASE-------------------------------------------------


    private void validarCampos(){


        String nome = editText_Nome.getText().toString();
        String contato = editText_NumeroContato.getText().toString();
        boolean whatsApp = checkBox_WhatsApp.isChecked();
        String email = editText_Email.getText().toString();
        boolean barba = checkBox_Barba.isChecked();
        boolean cabelo = checkBox_Cabelo.isChecked();


        if(!nome.isEmpty()){

            if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{


                if (Util.statusInternet_MoWi(getBaseContext())){



                    if (agendamento.getNome().equals(nome) && agendamento.getContato().equals(contato) &&
                    agendamento.isWhatsApp() == whatsApp && agendamento.getEmail().equals(email) &&
                    agendamento.isBarba() == barba && agendamento.isCabelo() == cabelo){

                        Toast.makeText(getBaseContext(),"Você não alterou nenhuma informação",Toast.LENGTH_LONG).show();


                    }else{

                        alterarDadosAgendamento(nome,contato,whatsApp,email,barba,cabelo);

                    }

                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
            }

        }else{

            Toast.makeText(getBaseContext(),"Insira seu nome para Agendar.",Toast.LENGTH_LONG).show();
        }

    }





    private void alterarDadosAgendamento(String nome,String contato,
                                         boolean whatsApp, String email, boolean barba, boolean cabelo){


        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"");

        Agendamento dadosAgendamento = new Agendamento(nome,contato,whatsApp,email,barba,cabelo);


        DatabaseReference databaseReference  = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0));


        Map<String, Object> atualizacao = new HashMap<>();


        atualizacao.put(data.get(3),dadosAgendamento);


        databaseReference.updateChildren(atualizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if(task.isSuccessful()){

                    dialogProgress.dismiss();

                    Toast.makeText(getBaseContext(),"Sucesso ao alterar dados",Toast.LENGTH_LONG).show();

                }else{

                    dialogProgress.dismiss();

                    Toast.makeText(getBaseContext(),"Erro ao alterar dados",Toast.LENGTH_LONG).show();
                }


            }
        });


    }




    //----------------------------------REMOVER NO FIREBASE-------------------------------------------------




    private void removerAgendamento(){


        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"");


        DatabaseReference databaseReference  = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0));




        databaseReference.child(data.get(3)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if(task.isSuccessful()){

                    dialogProgress.dismiss();
                    finish();
                    Toast.makeText(getBaseContext(),"Sucesso ao remover Agendamento",Toast.LENGTH_LONG).show();

                }else{

                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Erro ao remover Agendamento",Toast.LENGTH_LONG).show();
                }


            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (valueEventListener != null){


            reference.removeEventListener(valueEventListener);
            valueEventListener = null;

        }


    }
}
