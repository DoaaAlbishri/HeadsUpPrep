package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class AddCelebrity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_celebrity)

        val name = findViewById<View>(R.id.edName) as EditText
        val taboo1 = findViewById<View>(R.id.edTaboo1) as EditText
        val taboo2 = findViewById<View>(R.id.edTaboo2) as EditText
        val taboo3 = findViewById<View>(R.id.edTaboo3) as EditText
        val btAdd = findViewById<Button>(R.id.btAdd)
        val btBack = findViewById<Button>(R.id.btBack)
        btBack.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        btAdd.setOnClickListener {
            var user = CelebrityDetails.Celebrity(
                name.text.toString(),
                taboo1.text.toString(),
                taboo2.text.toString(),
                taboo3.text.toString(),
                Random.nextInt(0, 200)
            )

            addUserdata(user, onResult = {
                name.setText("")
                taboo1.setText("")
                taboo2.setText("")
                taboo3.setText("")
            })
        }
    }

    private fun addUserdata(user: CelebrityDetails.Celebrity, onResult: () -> Unit) {

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        if (apiInterface != null) {
            apiInterface.addCelebrity(user).enqueue(object : Callback<CelebrityDetails.Celebrity> {
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
}