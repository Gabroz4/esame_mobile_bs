package it.unibo.lam.shop.ui.productList

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import it.unibo.lam.shop.R
import it.unibo.lam.shop.data.product.Product

import it.unibo.lam.shop.databinding.FragmentProductRowBinding
import it.unibo.lam.shop.ui.productPage.ProductDetailsFragment


class MyProductLListRecyclerViewAdapter() : RecyclerView.Adapter<MyProductLListRecyclerViewAdapter.ViewHolder>() {

    var values: List<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentProductRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id.toString()
        holder.contentView.text = item.title 

        holder.itemView.setOnClickListener {
            Log.i("Adapter", "onBindViewHolder: element clicked " + position + item.id)

            val fragmentManager =  (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, ProductDetailsFragment.newInstance(item.id))
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount(): Int = values.size

    fun setProducts(products: List<Product>) {
        this.values = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentProductRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}