package com.maxxxwk.hometaskl11

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {
        private const val STRING_KEY = "STRING_KEY"
        private const val NUMBER_KEY = "NUMBER_KEY"

        fun start(string: String, number: Int, context: Context) {
            val intent = Intent(context, SecondActivity::class.java).apply {
                putExtra(STRING_KEY, string)
                putExtra(NUMBER_KEY, number)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        putDataInTextView()
    }

    private fun putDataInTextView() {
        val string = intent.getStringExtra(STRING_KEY)
        val number = intent.getIntExtra(NUMBER_KEY, 0)
        findViewById<TextView>(R.id.textView).text = "$string $number"
    }
}