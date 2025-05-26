package com.example.projeto_giovana2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var botaoRegistrar: Button
    private lateinit var botaoLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextSenha = findViewById(R.id.editTextSenha)
        botaoRegistrar = findViewById(R.id.botaoRegistrar)
        botaoLogin = findViewById(R.id.botaoLogin)

        botaoRegistrar.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()
            registrarUsuario(email, senha)
        }

        botaoLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()
            realizarLogin(email, senha)
        }
    }

    private fun registrarUsuario(email: String, senha: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                val msg = if (task.isSuccessful) "Registrado com sucesso!"
                else "Erro ao registrar: ${task.exception?.message}"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun realizarLogin(email: String, senha: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity2::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao logar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
