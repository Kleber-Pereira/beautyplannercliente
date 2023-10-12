package com.tcc.beautyplannercliente.funcoes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannercliente.R
import com.tcc.beautyplannercliente.Servicos.ServicosAdapter
import com.tcc.beautyplannercliente.Servicos.ServicosModel


class FuncoesBuscarServicoActivity : AppCompatActivity() {
    private lateinit var servicosRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var servicosList: ArrayList<ServicosModel>
   // private lateinit var funcionariosList: ArrayList<FuncionarioModel>

    private lateinit var dbRef: DatabaseReference
   // private lateinit var dbReffunc: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_servicos_funcoes)

        servicosRecyclerView = findViewById(R.id.rvservicosServico)
        servicosRecyclerView.layoutManager = LinearLayoutManager(this)
        servicosRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        servicosList = arrayListOf<ServicosModel>()

        getServicosData()
    }

    private fun getServicosData() {

        servicosRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Servicos")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                servicosList.clear()
                if (snapshot.exists()){
                    for (servicosSnap in snapshot.children){
                        val servicosData = servicosSnap.getValue(ServicosModel::class.java)
                        servicosList.add(servicosData!!)
                    }
                    val mAdapter = ServicosAdapter(servicosList)
                    servicosRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ServicosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {


                            val intent = Intent(this@FuncoesBuscarServicoActivity, FuncoesBuscarFuncionarioActivity::class.java)

                            //put extras
                            intent.putExtra("servicosId", servicosList[position].servicosId)
                            intent.putExtra("servicosServico", servicosList[position].vservicosServico)
                            intent.putExtra("servicosPreco", servicosList[position].vservicosPreco)

                            startActivity(intent)

                        }

                    }

                    )

                    servicosRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }



    /*private fun getFuncaoData() {

        funcaoRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Funcao")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcaoList.clear()
                if (snapshot.exists()){
                    for (funcaoSnap in snapshot.children){
                        val funcaoData = funcaoSnap.getValue(FuncaoModel::class.java)
                        funcaoList.add(funcaoData!!)
                    }
                    val mAdapter = FuncaoAdapter(funcaoList)
                    funcaoRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncaoAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FuncoesInsBuscarActivity, FuncaoDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("funcaoId", funcaoList[position].funcaoId)
                            intent.putExtra("funcaofuncionarioNome", funcaoList[position].vfuncaofuncionarioNome)
                            intent.putExtra("funcaoservicoNome", funcaoList[position].vfuncaoservicoNome)

                            startActivity(intent)

                        }

                    }

                    )

                    funcaoRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }*/
}
