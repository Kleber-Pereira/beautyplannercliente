package com.tcc.beautyplannercliente.agendamento

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannercliente.R
import com.tcc.beautyplannercliente.activity.AgendamentoServicoActivity
import com.tcc.beautyplannercliente.activity.HorariosActivity
import com.tcc.beautyplannercliente.fragment.FragmentCalendario
import com.tcc.beautyplannercliente.funcionario.FuncionarioAdapter
import com.tcc.beautyplannercliente.funcionario.FuncionarioModel

class FuncoesBuscarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcionarioRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
   // private lateinit var funcionarioList: ArrayList<FuncionarioModel>
    private lateinit var funcionarioList: ArrayList<FuncoesModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var funcoesservicoNome: String
    private lateinit var calendariodata : ArrayList<String>




    //private FuncoesBuscarServicoActivity fragmentFuncoes;
    private val fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_funcionario_funcoes)

        funcionarioRecyclerView = findViewById(R.id.rvfuncionario)
        funcionarioRecyclerView.layoutManager = LinearLayoutManager(this)
        funcionarioRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        funcionarioList = arrayListOf<FuncoesModel>()

        //funcoesservicoNome = ""
        funcoesservicoNome = intent.getStringExtra("servicoservico").toString()

        calendariodata = intent.getStringArrayListExtra("data") as ArrayList<String>




        getFuncionarioData()
    }
    private fun getFuncionarioData() {



        funcionarioRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Funcoes")



        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcionarioList.clear()
                if (snapshot.exists()){

                    for (funcionarioSnap in snapshot.children){
                        val funcionarioData = funcionarioSnap.getValue(FuncoesModel::class.java)
                        if (funcoesservicoNome == funcionarioData!!.vfuncoesservicoNome){
                            funcionarioList.add(funcionarioData!!)
                        }
                    }
                    val mAdapter = FuncoesFuncAdapter(funcionarioList)
                    funcionarioRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncoesFuncAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FuncoesBuscarFuncionarioActivity,
                                HorariosActivity::class.java)

                            //put extras
                            //intent.putExtra("funcionarioId", funcionarioList[position].funcionarioId)
                            intent.putExtra("funcionarioNome", funcionarioList[position].vfuncoesfuncionarioNome)
                            //intent.putExtra("funcionarioTelefone", funcionarioList[position].vfuncionarioTelefone)
                           // intent.putExtra("funcionarioEmail", funcionarioList[position].vfuncionarioEmail)
                            intent.putExtra("servicoservico",funcoesservicoNome)
                            //intent.putExtra("servicopreco",funcoesservicoPreco.toString())
                            intent.putExtra("data",calendariodata)


                            startActivity(intent)

                        }

                    }

                    )

                    funcionarioRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}
