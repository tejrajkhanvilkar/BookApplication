package com.internshala.bookapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookapplication.R
import com.internshala.bookapplication.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteAdapter(val context:Context,val booklist : List<BookEntity>) : RecyclerView.Adapter<FavouriteAdapter.Favviewholder>(){

    class Favviewholder(view:View):RecyclerView.ViewHolder(view){
        val txtBookName : TextView = view.findViewById(R.id.favbookname)
        val txtBookAuthor: TextView = view.findViewById(R.id.favbookauthor)
        val txtBookPrice : TextView = view.findViewById(R.id.favbookprice)
        val txtBookRating : TextView = view.findViewById(R.id.favbookrating)
        val txtBookImage : ImageView = view.findViewById(R.id.favbookimage)
        val contentlayout : RelativeLayout = view.findViewById(R.id.favcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favviewholder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_fav_single_row,parent,false)
        return Favviewholder(view)
    }

    override fun getItemCount(): Int {
       return booklist.size
    }

    override fun onBindViewHolder(holder: Favviewholder, position: Int) {

        val book=booklist[position]
        holder.txtBookName.text= book.bookname
        holder.txtBookAuthor.text= book.bookauthor
        holder.txtBookPrice.text= book.bookprice
        holder.txtBookRating.text= book.bookrating
        Picasso.get().load(book.bookimage).error(R.drawable.default_book_cover).into(holder.txtBookImage)

    }


}