package com.tcc.beautyplannercliente.activity;

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
import com.tcc.beautyplannercliente.util.DialogProgress;
import com.tcc.beautyplannercliente.util.Util;

import java.util.ArrayList;

public class AgendamentoServicoActivity extends AppCompatActivity implements View.OnClickListener{



    private EditText editText_Nome;
    private TextView textView_NumeroContato;
    private CheckBox checkBox_WhatsApp;
    private EditText editText_Email;
    private CheckBox checkBox_Barba;
    private CheckBox checkBox_Cabelo;
    private CardView cardView_Agendar;


    //private GoogleApiClient googleApiClient_Numero;


    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendemento_servico);



        data = getIntent().getStringArrayListExtra("data");


        editText_Nome = (EditText)findViewById(R.id.editText_AgendamentoServico_Nome);
        textView_NumeroContato = (TextView)findViewById(R.id.textView_AgendamentoServico_Numero);
        checkBox_WhatsApp = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_WhatsApp);
        editText_Email = (EditText)findViewById(R.id.editText_AgendamentoServico_Email);
        checkBox_Barba = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_barba);
        checkBox_Cabelo = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_Cabelo);
        cardView_Agendar = (CardView)findViewById(R.id.cardView_AgendamentoServico_Agendar);



        cardView_Agendar.setOnClickListener(this);



/*
        googleApiClient_Numero = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.CREDENTIALS_API)
                    .build();

*/

        obterNumeroContato();



        obterEmail();




    }


    //----------------------------------ACAO DE CLICK--------------------------------------------------

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case R.id.cardView_AgendamentoServico_Agendar:

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
        boolean barba = checkBox_Barba.isChecked();
        boolean cabelo = checkBox_Cabelo.isChecked();


        if(!nome.isEmpty()){

            if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{


                if (Util.statusInternet_MoWi(getBaseContext())){



                    agendarFirebase(nome,contato,whatsApp,email,barba,cabelo);


                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
            }

        }else{

            Toast.makeText(getBaseContext(),"Insira seu nome para Agendar.",Toast.LENGTH_LONG).show();
        }

    }



    private void agendarFirebase(String nome,String contato, boolean whatsApp, String email, boolean barba, boolean cabelo){


        Agendamento agendamento = new Agendamento(nome,contato,whatsApp,email,barba,cabelo);


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"dialog");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0));


        reference.child(data.get(3)).setValue(agendamento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()){


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
