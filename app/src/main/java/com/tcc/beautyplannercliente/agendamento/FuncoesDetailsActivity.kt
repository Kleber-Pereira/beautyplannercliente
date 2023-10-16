package com.tcc.beautyplannercliente.agendamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannercliente.R

class FuncoesDetailsActivity : AppCompatActivity() {
    private lateinit var tvfuncoesId: TextView
    private lateinit var tvfuncoesfuncionarioNome: TextView
    private lateinit var tvfuncoesservicosNome: TextView
   // private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcoes_details)

        initView()
        setValuesToViews()

        /*btnUpdate.setOnClickListener {
            openUpdateDialog(

                intent.getStringExtra("funcoesfuncionarioNome").toString(),
                intent.getStringExtra("funcoesservicosNome").toString()
            )
           // return@setOnClickListener


        }*/
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("funcoesId").toString()
            )
            //return@setOnClickListener
        }


    }

    /*private fun openUpdateDialog(
        funcoesId: String,
        funcoesfuncionarioNome: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_funcoes_dialog, null)

        mDialog.setView(mDialogView)

        val etfuncoesfuncionarioNome = mDialogView.findViewById<EditText>(R.id.etfuncaofuncionarioNome)
        val etfuncoesservicosNome = mDialogView.findViewById<EditText>(R.id.etfuncaoServicoNome)


        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etfuncoesfuncionarioNome.setText(intent.getStringExtra("funcoesfuncionarioNome").toString())
        etfuncoesservicosNome.setText(intent.getStringExtra("funcoesservicoNome").toString())


        mDialog.setTitle("Atualizando $funcoesfuncionarioNome ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateServicosData(
                funcoesId,
                etfuncoesfuncionarioNome.text.toString(),
                etfuncoesservicosNome.text.toString()

            )

            Toast.makeText(applicationContext, "Dados do servico atulizado", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews

            tvfuncoesfuncionarioNome.text = etfuncoesfuncionarioNome.text.toString()
            tvfuncoesservicosNome.text = etfuncoesservicosNome.text.toString()

            alertDialog.dismiss()
        }
    }*/

    /*private fun updateServicosData(
        funcoesId: String,
        vfuncoesfuncionarioNome: String,
        vfuncoesservicoNome: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcoes")
        val funcoesInfo = FuncoesModel(funcoesId,vfuncoesfuncionarioNome,vfuncoesservicoNome)
        dbRef.setValue(funcoesInfo)
    }*/

    private fun initView() {
        tvfuncoesId = findViewById(R.id.tvfuncoesId)
        tvfuncoesfuncionarioNome = findViewById(R.id.tvfuncoesfuncionarioNome)
        tvfuncoesservicosNome = findViewById(R.id.tvfuncoesservicosNome)
        //btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvfuncoesId.text = intent.getStringExtra("funcoesId")
        tvfuncoesfuncionarioNome.text = intent.getStringExtra("funcoesfuncionarioNome")
        tvfuncoesservicosNome.text = intent.getStringExtra("funcoesservicoNome")
    }
    private fun deleteRecord(id: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcoes").child(id)
        val mTask = dbRef.removeValue()


        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados do serviço excluídos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FuncoesConsultarServicoActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Erro ao Excluir ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
