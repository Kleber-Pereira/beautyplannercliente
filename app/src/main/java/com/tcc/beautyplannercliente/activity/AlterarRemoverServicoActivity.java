package com.tcc.beautyplannercliente.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.tcc.beautyplannercliente.modelo.TwilioService;
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
    /*private CheckBox checkBox_Barba;
    private CheckBox checkBox_Cabelo;*/
    private TextView editText_Servico;
    private TextView editText_Funcionario;
    private CardView cardView_Alterar;
    private CardView cardView_Remover;



    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    private Agendamento agendamento;

    private String funcoesservicoNome;
    private String funcoesfuncionarioNome;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_remover_servico);


        funcoesservicoNome = getIntent().getStringExtra("servicoservico");
        funcoesfuncionarioNome = getIntent().getStringExtra("funcionarioNome");
        data = getIntent().getStringArrayListExtra("data");

        database = FirebaseDatabase.getInstance();


        editText_Nome = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Nome);
        editText_NumeroContato = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Numero);
        checkBox_WhatsApp = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_WhatsApp);
        editText_Email = (EditText)findViewById(R.id.editText_AlterarRemeverServico_Email);
        /*checkBox_Barba = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_barba);
        checkBox_Cabelo = (CheckBox)findViewById(R.id.checkbox_AlterarRemeverServico_Cabelo);*/
        editText_Servico= (TextView) findViewById(R.id.editText_AlterarRemeverServico);
        editText_Funcionario = (TextView) findViewById(R.id.editText_AlterarRemeverFuncionario);
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
                child(data.get(1)).child("dia").child(data.get(0)).child(funcoesservicoNome).
                child(funcoesfuncionarioNome).child(data.get(3));
        /*reference = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(data.get(3)).child(funcoesservicoNome).
                child(funcoesfuncionarioNome).child(data.get(3));*/


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
        /*checkBox_Cabelo.setChecked(agendamento.isCabelo());
        checkBox_Barba.setChecked(agendamento.isBarba());*/
        editText_Servico.setText(agendamento.getServico());
        editText_Funcionario.setText(agendamento.getFuncionario());

    }



    //----------------------------------AGENDAR NO FIREBASE-------------------------------------------------


    private void validarCampos(){


        String nome = editText_Nome.getText().toString();
        String contato = editText_NumeroContato.getText().toString();
        boolean whatsApp = checkBox_WhatsApp.isChecked();
        String email = editText_Email.getText().toString();
        /*boolean barba = checkBox_Barba.isChecked();
        boolean cabelo = checkBox_Cabelo.isChecked();*/
        String servico = editText_Servico.getText().toString();
        String funcionario = editText_Funcionario.getText().toString();


        if(!nome.isEmpty()){

            /*if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{*/


                if (Util.statusInternet_MoWi(getBaseContext())){



                    if (agendamento.getNome().equals(nome) && agendamento.getContato().equals(contato) &&
                    agendamento.isWhatsApp() == whatsApp && agendamento.getEmail().equals(email) &&
                    agendamento.getServico() == servico && agendamento.getFuncionario() == funcionario){

                        Toast.makeText(getBaseContext(),"Você não alterou nenhuma informação",Toast.LENGTH_LONG).show();


                    }else{

                        alterarDadosAgendamento(nome,contato,whatsApp,email,servico,funcionario);

                    }

                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
           // }

        }else{

            Toast.makeText(getBaseContext(),"Insira seu nome para Agendar.",Toast.LENGTH_LONG).show();
        }

    }





    private void alterarDadosAgendamento(String nome,String contato,
                                         boolean whatsApp, String email, String servico, String funcionario){


        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getSupportFragmentManager(),"");

        Agendamento dadosAgendamento = new Agendamento(nome,contato,whatsApp,email,servico,funcionario);


        DatabaseReference databaseReference  = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(funcoesservicoNome).
                child(funcoesfuncionarioNome);


        Map<String, Object> atualizacao = new HashMap<>();


        atualizacao.put(data.get(3),dadosAgendamento);


        databaseReference.updateChildren(atualizacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if(task.isSuccessful()){

                    //----twilio

                    String telefone = contato;
                    String nome = editText_Nome.getText().toString();
                    String servico = editText_Servico.getText().toString();
                    String funcionario = editText_Funcionario.getText().toString();
                    String dia = (data.get(0) +"/"+data.get(1)+"/"+data.get(2));
                    String horario = (data.get(3));
                    // String accountSid = System.getenv("AC6abb957b4af10ab40428285f56f58add");
                    // String authToken = System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025");
                    /*Twilio.init(accountSid, authToken);
                    Message message = Message.creator(
                            new PhoneNumber(telefone),"MG3e0104f57d45974fd589e55519f447be",
                            nome + ", o serviço "+ servico + " com o profissional "+ funcionario +
                    " do dia " + data.get(0) +"/"+data.get(1)+"/"+data.get(2)+
                    " às "+ data.get(3) +" foi agendado com sucesso pelo BeautyPlanner!"
                    ).create();
                    System.out.println(message.getSid());*/

                   /* Twilio.init(System.getenv("AC6abb957b4af10ab40428285f56f58add"),
                            System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025"));
                    Message message = Message.creator(
                            new PhoneNumber("+5513974230860"),
                            new PhoneNumber("+12563848116"),
                            "teste").create();


                    System.out.println(message.getSid());*/
                    String mensagem = (nome + ", o serviço "+ servico + " com o profissional "
                            + funcionario +
                            " no dia "+ dia +
                            " às "+ horario +" foi atualizado com sucesso pelo BeautyPlanner!").toString();

                    TwilioService.sendSms(telefone, mensagem);

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
                child(data.get(1)).child("dia").child(data.get(0)).child(funcoesservicoNome).
                child(funcoesfuncionarioNome);

        //---------twilio

        String telefone = editText_NumeroContato.getText().toString();
        String nome = editText_Nome.getText().toString();
        String servico = editText_Servico.getText().toString();
        String funcionario = editText_Funcionario.getText().toString();
        String dia = (data.get(0) +"/"+data.get(1)+"/"+data.get(2));
        String horario = (data.get(3));

        //---------twilio


        databaseReference.child(data.get(3)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if(task.isSuccessful()){

                    //---------twilio

                    /*String telefone = editText_NumeroContato.getText().toString();
                    String nome = editText_Nome.getText().toString();
                    String servico = editText_Servico.getText().toString();
                    String funcionario = editText_Funcionario.getText().toString();
                    String dia = (data.get(0) +"/"+data.get(1)+"/"+data.get(2));
                    String horario = (data.get(3));*/
                    // String accountSid = System.getenv("AC6abb957b4af10ab40428285f56f58add");
                    // String authToken = System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025");
                    /*Twilio.init(accountSid, authToken);
                    Message message = Message.creator(
                            new PhoneNumber(telefone),"MG3e0104f57d45974fd589e55519f447be",
                            nome + ", o serviço "+ servico + " com o profissional "+ funcionario +
                    " do dia " + data.get(0) +"/"+data.get(1)+"/"+data.get(2)+
                    " às "+ data.get(3) +" foi agendado com sucesso pelo BeautyPlanner!"
                    ).create();
                    System.out.println(message.getSid());*/

                   /* Twilio.init(System.getenv("AC6abb957b4af10ab40428285f56f58add"),
                            System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025"));
                    Message message = Message.creator(
                            new PhoneNumber("+5513974230860"),
                            new PhoneNumber("+12563848116"),
                            "teste").create();


                    System.out.println(message.getSid());*/
                    String mensagem = (nome + ", o serviço "+ servico + " com o profissional "
                            + funcionario +
                            " no dia "+ dia +
                            " às "+ horario +" foi cancelado com sucesso pelo BeautyPlanner!").toString();

                    TwilioService.sendSms(telefone, mensagem);

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
