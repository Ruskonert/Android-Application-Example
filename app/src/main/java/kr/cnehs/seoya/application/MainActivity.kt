package kr.cnehs.seoya.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<View>(R.id.internetButton) as Button
        button.setOnClickListener {
            val intent = Intent(applicationContext, FirstActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<View>(R.id.foodButton) as Button
        button2.setOnClickListener {
            val intent2 = Intent(applicationContext, SecondActivity::class.java)
            startActivity(intent2)
        }
        val button3 = findViewById<View>(R.id.timetableButton) as Button
        button3.setOnClickListener {
            val intent3 = Intent(applicationContext, ThreeActivity::class.java)
            startActivity(intent3)
        }
    }

}
