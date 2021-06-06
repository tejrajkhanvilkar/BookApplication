package com.internshala.bookapplication.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.bookapplication.R
import com.internshala.bookapplication.adapter.DashboardRecyclerAdapter
import com.internshala.bookapplication.adapter.FavouriteAdapter
import com.internshala.bookapplication.database.BookDatabase
import com.internshala.bookapplication.database.BookEntity

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var recyclerfavourite : RecyclerView
    lateinit var progreslayout : RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteAdapter
    var dbbooklist = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerfavourite=view.findViewById(R.id.recyclerFavourite)
       // progreslayout=view.findViewById(R.id.favprogresslayout)
        //progressBar=view.findViewById(R.id.favprogressbar)

        //progreslayout.visibility =  View.VISIBLE

        layoutManager =LinearLayoutManager(activity)
        //layoutManager=GridLayoutManager(activity as Context,2)

        dbbooklist = RetrieveFavourite(activity as Context).execute().get()

        if(activity!=null){
         //   progreslayout.visibility = View.GONE
            recyclerAdapter = FavouriteAdapter(activity as Context,dbbooklist)
            recyclerfavourite.adapter = recyclerAdapter
            recyclerfavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavourite(val context: Context) : AsyncTask<Void,Void,List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"book_db").build()
            return db.bookDao().getallbooks()
        }
    }


}
