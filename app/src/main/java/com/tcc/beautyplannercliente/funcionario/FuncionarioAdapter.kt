package com.tcc.beautyplannercliente.funcionario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tcc.beautyplannercliente.R

class FuncionarioAdapter (private val funcionarioList: ArrayList<FuncionarioModel>) :
    RecyclerView.Adapter<FuncionarioAdapter.ViewHolder>() {

        //var onItemClick : ((funcionarioModel) -> Unit)? = null

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener{
            fun onItemClick(position: Int)

        }

        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listar_funcionario, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentFuncionario = funcionarioList[position]
            holder.tvfuncionarioNome.text = currentFuncionario.vfuncionarioNome


        }

        override fun getItemCount(): Int {
            return funcionarioList.size
        }

        class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

            val tvfuncionarioNome : TextView = itemView.findViewById(R.id.tvfuncionarioNome)

            init {
                itemView.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }

        }

    }