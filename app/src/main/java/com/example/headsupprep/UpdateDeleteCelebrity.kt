package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_update_delete_celebrity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class UpdateDeleteCelebrity : AppCompatActivity() {
    private var transid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_celebrity)
        val name = findViewById<View>(R.id.edUpdateName) as EditText
        val taboo1 = findViewById<View>(R.id.edUpdateTaboo1) as EditText
        val taboo2 = findViewById<View>(R.id.edUpdateTaboo2) as EditText
        val taboo3 = findViewById<View>(R.id.edUpdateTaboo3) as EditText
        val btDelete = findViewById<Button>(R.id.btDelete)
        val btUpdate = findViewById<Button>(R.id.btUpdate)
        val btBack1 = findViewById<Button>(R.id.btBack1)

        //get data from intent
        var intent = intent
        transid = intent.extras!!.getInt("id", 0)
       // val transid = intent.getIntExtra("id", 0)
        getCelebrity()

        btBack1.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btUpdate.setOnClickListener {
            var user = CelebrityDetails.Celebrity(
                name.text.toString(),
                taboo1.text.toString(),
                taboo2.text.toString(),
                taboo3.text.toString(),
                    transid
            )
            updateUserdata(user, onResult = {
                name.setText("")
                taboo1.setText("")
                taboo2.setText("")
                taboo3.setText("")
            })
        }

        btDelete.setOnClickListener {
            var user = CelebrityDetails.Celebrity(
                name.text.toString(),
                taboo1.text.toString(),
                taboo2.text.toString(),
                taboo3.text.toString(),
                    transid
            )

            deleteUserdata(user, onResult = {
                name.setText("")
                taboo1.setText("")
                taboo2.setText("")
                taboo3.setText("")
            })
        }
    }

    private fun updateUserdata(user: CelebrityDetails.Celebrity, onResult: () -> Unit) {

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        if (apiInterface != null) {
            apiInterface.updateCelebrity(transid,user).enqueue(object : Callback<CelebrityDetails.Celebrity> {
                override fun onResponse(
                    call: Call<CelebrityDetails.Celebrity>,
                    response: Response<CelebrityDetails.Celebrity>
                ) {
                    onResult()
                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<CelebrityDetails.Celebrity>, t: Throwable) {
                    onResult()
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()

                }
            })
        }
    }

    private fun deleteUserdata(user: CelebrityDetails.Celebrity, onResult: () -> Unit) {

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        if (apiInterface != null) {
            apiInterface.deleteCelebrity(transid).enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    onResult()
                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    onResult()
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()

                }
            })
        }
    }

    private fun getCelebrity(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        apiInterface!!.getCelebrity(transid).enqueue(object: Callback<CelebrityDetails.Celebrity> {
            override fun onResponse(call: Call<CelebrityDetails.Celebrity>, response: Response<CelebrityDetails.Celebrity>) {
                progressDialog.dismiss()
                val celebrity = response.body()!!
                edUpdateName.setText(celebrity.name)
                edUpdateTaboo1.setText(celebrity.taboo1)
                edUpdateTaboo2.setText(celebrity.taboo2)
                edUpdateTaboo3.setText(celebrity.taboo3)
            }

            override fun onFailure(call: Call<CelebrityDetails.Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }
}