package com.example.aprendendokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txtResult)

        val btnConverter = findViewById<Button>(R.id.btnConverter)

        btnConverter.setOnClickListener {
            converter()
        }
    }

    private fun converter(){
        val selectedCurrency = findViewById<RadioGroup>(R.id.radioGroup)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked) {
            R.id.radioUSD -> "USD"
            R.id.radioEUR -> "EUR"
            else          -> "CLP"
        }

        val editValor = findViewById<EditText>(R.id.editValor)

        val value = editValor.text.toString()

        if(value.isEmpty())
            return

        result.text = value
        result.visibility = View.VISIBLE

        Thread{
            val url = URL("https://economia.awesomeapi.com.br/last/${currency}-BRL")

            val conn = url.openConnection() as HttpsURLConnection

            try {
                val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)

                runOnUiThread{
                    val res = obj.getJSONObject("${currency}BRL").getDouble("high")

                    result.text = "R$${"%.2f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }

            } finally {
                conn.disconnect()
            }
        }.start()
    }
}
