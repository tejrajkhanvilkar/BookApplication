package com.internshala.bookapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookapplication.R
import com.internshala.bookapplication.activity.DescriptionActivity
import com.internshala.bookapplication.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*

class DashboardRecyclerAdapter(val context : Context,val itemlist :ArrayList<Book> ) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }//responsible for creating initial 10 view holders

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemlist[position]
       holder.txtBookName.text= book.bookName
        holder.txtBookAuthor.text= book.bookAuthor
        holder.txtBookPrice.text= book.bookPrice
        holder.txtBookRating.text= book.bookRating
        //holder.txtBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtBookImage)

        holder.contentlayout.setOnClickListener {
            val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }

    }//responsible for recycling and reusing

    class DashboardViewHolder (view: View) :RecyclerView.ViewHolder(view){
        val txtBookName :TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor:TextView= view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val txtBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val contentlayout : RelativeLayout = view.findViewById(R.id.contetlayout)
    }

}