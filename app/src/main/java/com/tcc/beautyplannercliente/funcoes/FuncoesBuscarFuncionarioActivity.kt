package com.tcc.beautyplannercliente.funcoes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannercliente.R
import com.tcc.beautyplannercliente.fragment.FragmentCalendario
import com.tcc.beautyplannercliente.funcionario.FuncionarioAdapter
import com.tcc.beautyplannercliente.funcionario.FuncionarioModel

class FuncoesBuscarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcionarioRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var funcionarioList: ArrayList<FuncionarioModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var funcoesservicoNome: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_funcionario_funcoes)

        funcionarioRecyclerView = findViewById(R.id.rvfuncionario)
        funcionarioRecyclerView.layoutManager = LinearLayoutManager(this)
        funcionarioRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        funcionarioList = arrayListOf<FuncionarioModel>()

        //funcoesservicoNome = ""
        funcoesservicoNome = intent.getStringExtra("servicosServico").toString()




        getFuncionarioData()
    }
    private fun getFuncionarioData() {

        funcionarioRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcionarioList.clear()
                if (snapshot.exists()){
                    for (funcionarioSnap in snapshot.children){
                        val funcionarioData = funcionarioSnap.getValue(FuncionarioModel::class.java)
                        funcionarioList.add(funcionarioData!!)
                    }
                    val mAdapter = FuncionarioAdapter(funcionarioList)
                    funcionarioRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncionarioAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FuncoesBuscarFuncionarioActivity, FragmentCalendario::class.java)

                            //put extras
                            intent.putExtra("funcionarioId", funcionarioList[position].funcionarioId)
                            intent.putExtra("funcionarioNome", funcionarioList[position].vfuncionarioNome)
                            intent.putExtra("funcionarioTelefone", funcionarioList[position].vfuncionarioTelefone)
                            intent.putExtra("funcionarioEmail", funcionarioList[position].vfuncionarioEmail)
                            intent.putExtra("servicoservico",funcoesservicoNome.toString())


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
