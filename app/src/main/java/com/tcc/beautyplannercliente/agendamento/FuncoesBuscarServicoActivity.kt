package com.tcc.beautyplannercliente.agendamento

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannercliente.R
import com.tcc.beautyplannercliente.Servicos.ServicosAdapter
import com.tcc.beautyplannercliente.Servicos.ServicosModel


class FuncoesBuscarServicoActivity  : AppCompatActivity() {
    private lateinit var servicosRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var servicosList: ArrayList<ServicosModel>
   // private lateinit var servicosList: ArrayList<FuncoesModel>
    //private lateinit var funcoesList: ArrayList<FuncoesModel>
   // private lateinit var servicosiguaisList: ArrayList<FuncoesModel>
   // private lateinit var funcionariosList: ArrayList<FuncionarioModel>

    private lateinit var dbRef: DatabaseReference
    //private lateinit var dbRef2: DatabaseReference


    private lateinit var calendariodata : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_servicos_funcoes)

        servicosRecyclerView = findViewById(R.id.rvservicosServico)
        servicosRecyclerView.layoutManager = LinearLayoutManager(this)
        servicosRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        servicosList = arrayListOf<ServicosModel>()
       // servicosList = arrayListOf<FuncoesModel>()
      //  funcoesList = arrayListOf<FuncoesModel>()
     //   servicosiguaisList = arrayListOf<ServicosModel>()


        calendariodata = intent.getStringArrayListExtra("data") as ArrayList<String>

        getServicosData()
    }

    private fun getServicosData() {

        servicosRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Servicos")
       // dbRef = FirebaseDatabase.getInstance().getReference("Funcoes")
      //  dbRef2 = FirebaseDatabase.getInstance().getReference("Funcoes")

        dbRef.addValueEventListener(object : ValueEventListener {
            //@RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                servicosList.clear()
                if (snapshot.exists()){
                    for (servicosSnap in snapshot.children){

                        val servicosData = servicosSnap.getValue(ServicosModel::class.java)
                       // val servicosData = servicosSnap.getValue(FuncoesModel::class.java)
                        servicosList.add(servicosData!!)




                    }
                    //looping
                     /*dbRef2.addValueEventListener(object:ValueEventListener{
                         override fun onDataChange(snapshot2: DataSnapshot) {
                             funcoesList.clear()
                             if(snapshot2.exists()){
                                 for (funcoesSnap in snapshot2.children){
                                     //val servicosData = servicosSnap.getValue(ServicosModel::class.java)
                                     val funcoesData = funcoesSnap.getValue(FuncoesModel::class.java)
                                     *//*if ((servicosData!!.vservicosServico) ==(funcoesData!!.vfuncoesservicoNome)){
                                         servicosList.add(servicosData!!)

                                     }*//*
                                     funcoesList.add(funcoesData!!)
                                 }

                             }
                         }

                         override fun onCancelled(error: DatabaseError) {
                             TODO("Not yet implemented")
                         }

                     })*/

                   /* for (item1 in servicosList) {
                        //item1.vservicosServico
                        //val servicoData = item1.vservicosServico
                        for (item2 in funcoesList) {
                            //item2.vfuncoesservicoNome
                            //val funcoesData = item2.vfuncoesservicoNome
                            if (item1.vservicosServico.equals(item2.vfuncoesservicoNome)) {
                                servicosiguaisList.add(item1)
                            }
                        }
                    }*/




                    //looping
                    //val mAdapter = FuncoesAdapter(servicosList)
                    val mAdapter = ServicosAdapter(servicosList)
                    servicosRecyclerView.adapter = mAdapter

                    //mAdapter.setOnItemClickListener(object : FuncoesAdapter.onItemClickListener{
                    mAdapter.setOnItemClickListener(object : ServicosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {


                            val intent = Intent(this@FuncoesBuscarServicoActivity, FuncoesBuscarFuncionarioActivity::class.java)

                            //put extras
                           // intent.putExtra("servicosId", servicosList[position].servicosId)
                           // intent.putExtra("servicoservico", servicosList[position].vfuncoesservicoNome)
                            intent.putExtra("servicoservico", servicosList[position].vservicosServico)
                           // intent.putExtra("servicosPreco", servicosList[position].vservicosPreco)
                            intent.putExtra("data",calendariodata)

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
