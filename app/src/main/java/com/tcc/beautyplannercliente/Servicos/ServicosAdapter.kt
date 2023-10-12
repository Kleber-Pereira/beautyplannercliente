package com.tcc.beautyplannercliente.Servicos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tcc.beautyplannercliente.R

class ServicosAdapter (private val servicosList: ArrayList<ServicosModel>) :
    RecyclerView.Adapter<ServicosAdapter.ViewHolder>() {

        //var onItemClick : ((ServicosModel) -> Unit)? = null

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener{
            fun onItemClick(position: Int)

        }

        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listar_servicos, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentServicos = servicosList[position]
            holder.tvservicosServico.text = currentServicos.vservicosServico


        }

        override fun getItemCount(): Int {
            return servicosList.size
        }

        class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

            //val tvservicosServico : TextView = itemView.findViewById(R.id.tvservicosServico)
            val tvservicosServico : TextView = itemView.findViewById(R.id.tvservicosServico)
            init {
                itemView.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }

        }

    }