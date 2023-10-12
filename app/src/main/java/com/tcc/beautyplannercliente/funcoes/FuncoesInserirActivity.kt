package com.tcc.beautyplannercliente.funcoes

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.tcc.beautyplannerpro.R
import java.text.SimpleDateFormat
import java.util.*

class FuncoesInserirActivity : AppCompatActivity() {
    private lateinit var funcoesfuncionarioNome: TextView
    private lateinit var funcoesservicoNome: TextView
   // private lateinit var spinnerFuncionario: Spinner



    private lateinit var btnSalvarFuncao: Button

    private lateinit var dbRef: DatabaseReference

   /* private lateinit var funcionariosList: ArrayList<FuncionarioModel>
    private lateinit var dbReffunc: DatabaseReference*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_funcoes)


        funcoesfuncionarioNome = findViewById(R.id.funcoesfuncionarioNome)
        funcoesfuncionarioNome.text = intent.getStringExtra("funcionarioNome")

        funcoesservicoNome = findViewById(R.id.funcoesservicoNome)
        funcoesservicoNome.text = intent.getStringExtra("servicoservico")

        //spinnerFuncionario = findViewById(R.id.spinnerFuncionario)

        //funcionariosList = arrayListOf<FuncionarioModel>()


        //montar o spinner funcionarios
        /*dbReffunc = FirebaseDatabase.getInstance().getReference("Funcionarios").child("funcionarioID")
        dbReffunc.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcionariosList.clear()
                //if (snapshot.exists()){
                    for (funcionarioSnap in snapshot.children){
                        val funcionarioData = funcionarioSnap.getValue(FuncionarioModel::class.java)
                        funcionariosList.add(funcionarioData!!)
                    }
                *//*for (DataSnapshot in snapshot.children) {
                    val spinnerFunc =
                        snapshot.child("vfuncionarioNome").value.toString()
                    funcionariosList.add(spinnerFunc)
                }*//*
                    val arrayAdapter = ArrayAdapter(
                        this@FuncoesInserirActivity,
                        android.R.layout.simple_spinner_item,
                        funcionariosList
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    spinnerFuncionario.adapter


            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/

        btnSalvarFuncao= findViewById(R.id.btnSalvarFuncao)

        dbRef = FirebaseDatabase.getInstance().getReference("Funcoes")

        btnSalvarFuncao.setOnClickListener {
            salvarDadosFuncao()
            return@setOnClickListener
        }

    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }


    private fun salvarDadosFuncao() {

        //getting values
        val vfuncoesfuncionarioNome = funcoesfuncionarioNome.text.toString()
        val vfuncoesservicoNome = funcoesservicoNome.text.toString()




        if (vfuncoesfuncionarioNome.isEmpty()) {
            funcoesfuncionarioNome.error = "Inserir Funcionário"
        }

        else if (vfuncoesservicoNome.isEmpty()) {
            funcoesservicoNome.error = "Inserir Serviço"
        }

        else {
            val funcoesId = dbRef.push().key!!

            val funcoes = FuncoesModel(
                funcoesId,
                vfuncoesfuncionarioNome,vfuncoesservicoNome )

            /*val reference: DatabaseReference =
                firebaseDatabase.getReference().child("BD").child("Calendario")
                    .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                    .child("dia").child(data.get(0))*/

            dbRef.child(funcoesId).setValue(funcoes)
                .addOnCompleteListener {
                    Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show()

                   /* funcoesfuncionarioNome.text
                    funcoesservicoNome.text*/

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
}