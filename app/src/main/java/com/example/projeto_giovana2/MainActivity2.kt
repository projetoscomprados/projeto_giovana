package com.example.projeto_giovana2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity2 : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiKey = "AIzaSyB1z9PmxI0uHEXQUjFRJteQAvIYKXwyJT4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val btnDesculpas = findViewById<Button>(R.id.btnDesculpas)
        val btnPresente = findViewById<Button>(R.id.btnPresente)
        val textoDesculpas = findViewById<TextView>(R.id.textViewDesculpas2)
        val textoPresente = findViewById<TextView>(R.id.textviewPresente2)

        btnDesculpas.setOnClickListener {
            val prompt = "Crie um pedido curto e fofo de desculpas para uma namorada(o)."
            gerarResposta(prompt) { resposta -> runOnUiThread { textoDesculpas.text = resposta } }
        }

        btnPresente.setOnClickListener {
            val prompt = "Fale um presente curto e criativo para pedir desculpas para namorado(a)."
            gerarResposta(prompt) { resposta -> runOnUiThread { textoPresente.text = resposta } }
        }
    }

    private fun gerarResposta(pergunta: String, callback: (String) -> Unit) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", pergunta)
                ))
            ))
        }

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Erro: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val resposta = response.body?.string() ?: ""
                    val texto = JSONObject(resposta)
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    callback(texto)
                } catch (e: Exception) {
                    callback("Erro ao processar: ${e.message}")
                }
            }
        })
    }
}
