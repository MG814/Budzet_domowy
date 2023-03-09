package com.example.mjbudet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mjbudet.databinding.ItemViewBinding
import com.example.mjbudet.room.Transaction

class MyAdapter(private val transactions: List<Transaction>,
                private val onClick: (Transaction, Int) -> Unit): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(binding: ItemViewBinding): RecyclerView.ViewHolder(binding.root){
        val category = binding.category
        val type = binding.type
        val date = binding.date
        val value = binding.value
        val icon = binding.imageView
        val cardView = binding.cardView

        init {
            binding.root.setOnClickListener {
                onClick(transactions[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val typeIconResource = when(transactions[position].type){
            "Wydatki" -> R.drawable.dolar_down
            else -> {R.drawable.dollar_up}
        }

        if(transactions[position].type == "Wydatki"){
            holder.value.text = "-" + transactions[position].value.toString() + " PLN"
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.context, R.color.red_200))
        }
        else{
            holder.value.text = transactions[position].value.toString() + " PLN"
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.context, R.color.purple_200))

        }
        holder.category.text = transactions[position].category
        holder.type.text = transactions[position].type
        holder.date.text = transactions[position].date
        holder.icon.setImageResource(typeIconResource)

    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
