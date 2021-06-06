package com.internshala.bookapplication.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookapplication.R
import com.internshala.bookapplication.database.BookDatabase
import com.internshala.bookapplication.database.BookEntity
import com.internshala.bookapplication.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.*
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtbookimg : ImageView
    lateinit var txtbookname : TextView
    lateinit var txtbookauthor : TextView
    lateinit var txtbookprice : TextView
    lateinit var txtbookrating : TextView
    lateinit var txtbookdesc : TextView
    lateinit var btnfav : Button
    lateinit var progresslayout : RelativeLayout
    lateinit var progressBar: ProgressBar
    var bookidd : String? ="100"
    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtbookimg = findViewById(R.id.imgBookImageD)
        txtbookname = findViewById(R.id.txtBookNameD)
        txtbookauthor=findViewById(R.id.txtBookAuthorD)
        txtbookprice=findViewById(R.id.txtBookPriceD)
        txtbookrating=findViewById(R.id.txtBookRatingD)
        txtbookdesc=findViewById(R.id.txtbookdescriptionD)
        btnfav=findViewById(R.id.addtofavD)
        progresslayout=findViewById(R.id.progresslayoutD)
        progressBar=findViewById(R.id.progressbarD)
        progressBar.visibility = View.VISIBLE
        progresslayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.descriptionToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if(intent!=null){
            bookidd=intent.getStringExtra("book_id")
        }else{
            Toast.makeText(this@DescriptionActivity,"unexpected error occured",Toast.LENGTH_SHORT).show()
        }

        if(bookidd=="100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"unexpected error occured",Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"

        val jsonparams = JSONObject()
        jsonparams.put("book_id",bookidd)

        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonparams, Response.Listener {

                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progresslayout.visibility = View.GONE

                            val bookImageurl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.bookappicon).into(txtbookimg)
                            txtbookname.text = bookJsonObject.getString("name")
                            txtbookauthor.text = bookJsonObject.getString("author")
                            txtbookprice.text = bookJsonObject.getString("price")
                            txtbookrating.text = bookJsonObject.getString("rating")
                            txtbookdesc.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookidd?.toInt() as Int,
                                txtbookname.text.toString(),
                                        txtbookauthor.text.toString(),
                                        txtbookprice.text.toString(),
                                        txtbookrating.text.toString(),
                                        txtbookdesc.text.toString(),
                                bookImageurl
                            )

                            val checkfav = DBAsynctask(applicationContext,bookEntity,1).execute()
                            val isFav=checkfav.get()
                            if(isFav){
                                btnfav.text="Remove From Favourite"
                                val favclr = ContextCompat.getColor(applicationContext,R.color.colorfav)
                                btnfav.setBackgroundColor(favclr)
                            }else{
                                btnfav.text="Add to Favourite"
                                val nofavclr = ContextCompat.getColor(applicationContext,R.color.colorPrimaryDark)
                                btnfav.setBackgroundColor(nofavclr)
                            }

                            btnfav.setOnClickListener {
                                if(!DBAsynctask(applicationContext,bookEntity,1).execute().get()){

                                    val async = DBAsynctask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()

                                    if(result){
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book added to favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnfav.text="Remove from favourite"
                                        val favcolor = ContextCompat.getColor(applicationContext,R.color.colorfav)
                                        btnfav.setBackgroundColor(favcolor)
                                    }else{
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "some error occured",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }else{
                                    val async = DBAsynctask(applicationContext,bookEntity,3).execute()
                                    val result = async.get()

                                    if(result){
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "book removed from favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnfav.text="Add to Favourite"
                                        val nofavclr = ContextCompat.getColor(applicationContext,R.color.colorPrimaryDark)
                                        btnfav.setBackgroundColor(nofavclr)

                                    }else{
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "some error occured",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "unexpected error occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "catch error occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content-type"] = "application/json"
                        headers["token"] = "ca8c4dbef672d6"
                        return headers
                    }
                }

            queue.add(jsonRequest)
        }else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, Listner -> //
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit App") { text, Listner -> //
                ActivityCompat.finishAffinity(this@DescriptionActivity) //helps to exit app from any point
            }
            dialog.create()
            dialog.show()
        }
    }


    class DBAsynctask(val context: Context,val bookEntity: BookEntity,val mode:Int) : AsyncTask<Void,Void,Boolean>(){

        /*
        mode 1 -> check if the book i fav or not
        mode 2-> add to fav
        mode 3-> remove from fav
         */
        val db = Room.databaseBuilder(context,BookDatabase::class.java,"book_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 -> {
                    // check if bok is fav or not
                    val book:BookEntity? = db.bookDao().getbookbyid(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2 -> {
                    // add to fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove from fav
                    db.bookDao().deletebook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}
