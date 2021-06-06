package com.internshala.bookapplication.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookapplication.R
import com.internshala.bookapplication.adapter.DashboardRecyclerAdapter
import com.internshala.bookapplication.model.Book
import com.internshala.bookapplication.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
   // val booklist = arrayListOf("book A","book B","book C","book D","book E","book F","book G","book H","book I","book J")
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout :RelativeLayout
    lateinit var progressBar: ProgressBar

    val bookInfoList = arrayListOf<Book>( )
    var ratingcomparator = Comparator<Book>{book1,book2 ->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){

            book1.bookName.compareTo(book2.bookName,true)

        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)


        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        layoutManager = LinearLayoutManager(activity)

        progressLayout  = view.findViewById(R.id.progresslayout)
        progressBar = view.findViewById(R.id.progressbar)
        progressLayout.visibility = View.VISIBLE





        val queue = Volley.newRequestQueue(activity as Context) //queue of requests

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    //here we will handle the responses
                    //  println("Response is $it")//  "it" is the variable in which reaponse is stored
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookjsonobj = data.getJSONObject(i)
                            val bookobj = Book(
                                bookjsonobj.getString("book_id"),
                                bookjsonobj.getString("name"),
                                bookjsonobj.getString("author"),
                                bookjsonobj.getString("rating"),
                                bookjsonobj.getString("price"),
                                bookjsonobj.getString("image")

                            )
                            bookInfoList.add(bookobj)
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager



                        }
                    } else {
                        Toast.makeText(activity as Context, "Error occured", Toast.LENGTH_SHORT)
                            .show()
                    }

                }catch (e:JSONException){
                    Toast.makeText(activity as Context, "some unexpexted Error occured", Toast.LENGTH_SHORT)
                        .show()
                }
                }, Response.ErrorListener {

                    //gere we will handle the errors
                Toast.makeText(activity as Context, "Volley Error occured", Toast.LENGTH_SHORT).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content-type"] = "application/json"
                        headers["token"] = "ca8c4dbef672d6"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }else{
            val dialog =AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, Listner -> //
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit App") { text, Listner -> //
                ActivityCompat.finishAffinity(activity as Activity) //helps to exit app from any point
            }
            dialog.create()
            dialog.show()
        }




        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingcomparator)//this will sort in ascending order but we need descending order
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}
