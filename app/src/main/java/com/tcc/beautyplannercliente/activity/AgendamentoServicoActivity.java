package com.tcc.beautyplannercliente.activity;

import static com.tcc.beautyplannercliente.R.id;
import static com.tcc.beautyplannercliente.R.layout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.tcc.beautyplannercliente.R;
import com.tcc.beautyplannercliente.modelo.Agendamento;
import com.tcc.beautyplannercliente.modelo.SalvaCliente;
import com.tcc.beautyplannercliente.modelo.TwilioService;
import com.tcc.beautyplannercliente.util.DialogProgress;
import com.tcc.beautyplannercliente.util.Util;

import java.util.ArrayList;

public class AgendamentoServicoActivity extends AppCompatActivity implements View.OnClickListener{



    private EditText editText_Nome;
    private TextView textView_NumeroContato;
    private CheckBox checkBox_WhatsApp;
    private EditText editText_Email;
    /*private CheckBox checkBox_Barba;
    private CheckBox checkBox_Cabelo;*/
    private TextView editText_Servico;
    private TextView editText_Funcionario;
    private CardView cardView_Agendar;

    private String funcoesservicoNome;
    private String funcoesfuncionarioNome;


    //private GoogleApiClient googleApiClient_Numero;


    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_agendemento_servico);



        data = getIntent().getStringArrayListExtra("data");
        funcoesservicoNome = getIntent().getStringExtra("servicoservico");
        funcoesfuncionarioNome = getIntent().getStringExtra("funcionarioNome");


        editText_Nome = (EditText)findViewById(id.editText_AgendamentoServico_Nome);
        textView_NumeroContato = (TextView)findViewById(id.textView_AgendamentoServico_Numero);
        checkBox_WhatsApp = (CheckBox)findViewById(id.checkbox_AgendamentoServico_WhatsApp);
        editText_Email = (EditText)findViewById(id.editText_AgendamentoServico_Email);
        /*checkBox_Barba = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_barba);
        checkBox_Cabelo = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_Cabelo);*/
        editText_Servico = (TextView) findViewById(R.id.editText_Servico);
        editText_Funcionario = (TextView) findViewById(R.id.editText_Funcionario);
        cardView_Agendar = (CardView)findViewById(id.cardView_AgendamentoServico_Agendar);



        cardView_Agendar.setOnClickListener(this);



/*
        googleApiClient_Numero = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.CREDENTIALS_API)
                    .build();

*/

        obterNumeroContato();


        editText_Servico.setText(funcoesservicoNome);
        editText_Funcionario.setText(funcoesfuncionarioNome);




        obterEmail();




    }


    //----------------------------------ACAO DE CLICK--------------------------------------------------

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case id.cardView_AgendamentoServico_Agendar:

                agendar();

                break;

        }

    }






    //----------------------------------OBTER NUMERO TELEFONE-------------------------------------------------

    private void obterNumeroContato(){


        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder().setShowCancelButton(false).build())
                .setPhoneNumberIdentifierSupported(true)
                .build();


       /* GoogleApiClient googleApiClient_Numero = null;
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient_Numero,hintRequest);*/

        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);


        try {

            startIntentSenderForResult(intent.getIntentSender(),122,null,0,0,0);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }



    //----------------------------------OBTER EMAIL-------------------------------------------------


    private void obterEmail(){


        AccountManager accountManager = AccountManager.get(this);

        Account[] accounts = accountManager.getAccounts();


        for (Account account: accounts){

            String email = account.name;

            if(email.contains("@")){


                editText_Email.setText(email);
                break;

            }
        }

    }



    //----------------------------------METODOS OBTER NUMERO TELEFONE-------------------------------------------------



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 122) {


            if (resultCode == RESULT_OK) {

                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                if (!credential.getId().isEmpty()) {

                    textView_NumeroContato.setText(credential.getId());

                } else {

                    Toast.makeText(getBaseContext(), "Escolha um numero de contato para poder continuar", Toast.LENGTH_LONG).show();

                }
            } else {

                dialogNumeroContato();

            }

        }

    }


    private void dialogNumeroContato(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Escolha Obrigatória")
                .setCancelable(false)
                .setMessage("Escolha um número de telefone para agendar um horário.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        obterNumeroContato();
                    }
                })
                .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                });


        builder.show();

    }



    //----------------------------------AGENDAR NO FIREBASE-------------------------------------------------


    private void agendar(){



        String nome = editText_Nome.getText().toString();
        String contato = textView_NumeroContato.getText().toString();
        boolean whatsApp = checkBox_WhatsApp.isChecked();
        String email = editText_Email.getText().toString();
        /*boolean barba = checkBox_Barba.isChecked();
        boolean cabelo = checkBox_Cabelo.isChecked();*/
        String servico = editText_Servico.getText().toString();
        //String servico = funcoesservicoNome;
        String funcionario = editText_Funcionario.getText().toString();
        //String funcionario = funcoesfuncionarioNome;

        if(!nome.isEmpty()){

            /*if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{*/


                if (Util.statusInternet_MoWi(getBaseContext())){



                    agendarFirebase(nome,contato,whatsApp,email,servico,funcionario);


                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
           // }

        }else{

            Toast.makeText(getBaseContext(),"Insira seu nome para Agendar.",Toast.LENGTH_LONG).show();
        }

    }



    private void agendarFirebase(String nome,String contato, boolean whatsApp, String email, String servico, String funcionario){


        Agendamento agendamento = new Agendamento(nome,contato,whatsApp,email,servico,funcionario);
        SalvaCliente salvacliente = new SalvaCliente(nome,contato,email,servico);


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"dialog");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0)).child(funcoesservicoNome).child(funcoesfuncionarioNome);



        /*DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0));*/


        reference.child(data.get(3)).setValue(agendamento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if (task.isSuccessful()){

                    DatabaseReference referencecliente = firebaseDatabase.getReference().child("Cliente").child("Servico")
                            .child(funcoesservicoNome).child(contato);
                    referencecliente.setValue(salvacliente);

                    //----twillo---


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
                            " às "+ horario +" foi agendado com sucesso pelo BeautyPlanner!").toString();

                    TwilioService.sendSms(telefone, mensagem);

                   /*TwilioService.sendSms(telefone, nome + ", o serviço "+ servico + " com o profissional "+ funcionario +
                            " no dia "+ dia +
                            " às "+ horario +" foi agendado com sucesso pelo BeautyPlanner!");*/

                    //TwilioService.sendSms(telefone, "agendado com sucesso pelo BeautyPlanner!");


                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Sucesso ao Agendar",Toast.LENGTH_LONG).show();

                    finish();





                }else{

                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Falha ao Agendar",Toast.LENGTH_LONG).show();

                }



            }
        });

    }











}
