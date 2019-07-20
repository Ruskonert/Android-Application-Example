package kr.cnehs.seoya.application

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class FirstActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val naverButton = findViewById<View>(R.id.naverButton) as Button
        val schButton = findViewById<View>(R.id.schButton) as Button
        val googleButton = findViewById<View>(R.id.googleButton) as Button
        val translateButton = findViewById<View>(R.id.translateButton) as Button

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.naverButton -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.mail.naver.com/mobile"))
                    // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                    startActivity(intent)
                    finish()  // 브라우저만 띄우고 종료
                }
                R.id.schButton -> {
                    val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("http://mail.sch.ac.kr"))
                    // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                    startActivity(intent2)
                    finish()  // 브라우저만 띄우고 종료
                }

                R.id.googleButton -> {
                    val intent3 = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com"))
                    // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                    startActivity(intent3)
                    finish()  // 브라우저만 띄우고 종료
                }

                R.id.translateButton -> {
                    val intent4 = Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.google.co.kr"))

                    // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                    startActivity(intent4)

                    // 브라우저만 띄우고 종료
                    finish()
                }
            }
        }
        naverButton.setOnClickListener(listener)
        schButton.setOnClickListener(listener)
        googleButton.setOnClickListener(listener)
        translateButton.setOnClickListener(listener)
    }
}
