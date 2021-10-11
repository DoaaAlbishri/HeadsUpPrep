package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val words = arrayListOf<String>()
    lateinit var myRv: RecyclerView
    lateinit var edCelebrity : EditText
    var transID : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btAdd = findViewById<Button>(R.id.btAdd)
        val btSubmit = findViewById<Button>(R.id.btSubmit)
        edCelebrity = findViewById<EditText>(R.id.edCelebrity)
        myRv = findViewById(R.id.recyclerView)

        btSubmit.setOnClickListener {
            updateID()
        }

        fill()

        btAdd.setOnClickListener {
            intent = Intent(applicationContext, AddCelebrity::class.java)
            startActivity(intent)
        }

    }

    private fun fill(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        //progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val call: Call<List<CelebrityDetails.Celebrity>> = apiInterface!!.getCelebrity()

        call?.enqueue(object : Callback<List<CelebrityDetails.Celebrity>> {
            override fun onResponse(
                    call: Call<List<CelebrityDetails.Celebrity>>,
                    response: Response<List<CelebrityDetails.Celebrity>>
            ) {
                progressDialog.dismiss()
                val resource: List<CelebrityDetails.Celebrity>? = response.body()
                var userData: String? = "";
                for (User in resource!!) {
                    userData = userData + User.name + "\n" + "\n" + User.taboo1 + "\n" + User.taboo2 + "\n" +User.taboo3 + "\n" + "\n"
                }
                words.add(userData.toString())
                rv()
            }

            override fun onFailure(call: Call<List<CelebrityDetails.Celebrity>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun updateID(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<List<CelebrityDetails.Celebrity>> = apiInterface!!.getCelebrity()

        call?.enqueue(object : Callback<List<CelebrityDetails.Celebrity>> {
            override fun onResponse(
                    call: Call<List<CelebrityDetails.Celebrity>>,
                    response: Response<List<CelebrityDetails.Celebrity>>
            ) {
                val resource: List<CelebrityDetails.Celebrity>? = response.body()
                for (User in resource!!) {
                    if(edCelebrity.text.toString().capitalize() == User.name){
                        transID= User.pk!!
                        intent = Intent(applicationContext, UpdateDeleteCelebrity::class.java)
                        intent.putExtra("id",transID)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<List<CelebrityDetails.Celebrity>>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
            }
        })
    }

    fun rv() {
        myRv.adapter = RecyclerViewAdapter(words)
        myRv.layoutManager = LinearLayoutManager(this)
    }

}