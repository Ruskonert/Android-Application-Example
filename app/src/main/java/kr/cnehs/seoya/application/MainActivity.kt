package kr.cnehs.seoya.application

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    private lateinit var etShow: EditText
    private lateinit var etResult: EditText
    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var mul: Button
    private lateinit var div: Button
    private lateinit var remainder: Button
    private lateinit var del: Button
    private lateinit var result: Button

    //이번 연산의 결과를 저장
    private var history = ""
    //피연산자1
    private var number1 = ""
    //피연산자2
    private var number2 = ""

    //어떤 연산자가 선택되었는지 확인하기 위한 int형 type 변수
    private var type: Int = 0
    companion object {
        private const val ADD = 0
        private const val SUB = 1
        private const val MUL = 2
        private const val DIV = 3
        private const val REMAINDER = 4
    }
    
    private var d1: Double = 0.toDouble()
    private var d2: Double = 0.toDouble()

    private val listener : View.OnClickListener = View.OnClickListener {
        number1 = etResult.text.toString()
        when(it.id) {
            R.id.btn_add -> { history = etResult.text.toString() + " + "; type = ADD }
            R.id.btn_sub -> { history = etResult.text.toString() + " - "; type = SUB }
            R.id.btn_mul -> { history = etResult.text.toString() + " * "; type = MUL }
            R.id.btn_div -> { history = etResult.text.toString() + " / "; type = DIV }
            R.id.btn_remainder -> { history = etResult.text.toString() + " % "; type = REMAINDER }
        }
        etShow.setText(history)
        etResult.setText("")
    }

    private var listener2: View.OnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_del -> {
                val delNumber = etResult.text.toString()
                Toast.makeText(this@MainActivity, delNumber, Toast.LENGTH_SHORT).show()
                etResult.setText(delNumber.substring(0, delNumber.length - 1))
            }

            R.id.btn_result -> {
                number2 = etResult.text.toString()
                try {
                    d1 = number1.toDouble()
                    d2 = number2.toDouble()
                }
                catch(e : NumberFormatException) {
                    Toast.makeText(this@MainActivity, "먼저 수를 입력하세요.", Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }
                val result: Double = when (type) {
                    ADD -> d1 + d2
                    SUB -> d1 - d2
                    MUL -> d1 * d2
                    DIV -> d1 / d2
                    REMAINDER -> d1 % d2
                    else -> -123.4567890
                }
                Toast.makeText(this@MainActivity, "결과", Toast.LENGTH_SHORT).show()
                number2 = etResult.text.toString()
                history += etResult.text.toString()
                etShow.setText(history)
                etResult.setText(result.toString())
                number1 = etResult.text.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etShow = findViewById(R.id.et_show)
        etResult = findViewById(R.id.et_result)
        etResult.setText("")

        add = findViewById(R.id.btn_add)
        sub = findViewById(R.id.btn_sub)
        mul = findViewById(R.id.btn_mul)
        div = findViewById(R.id.btn_div)
        remainder = findViewById(R.id.btn_remainder)
        del = findViewById(R.id.btn_del)
        result = findViewById(R.id.btn_result)

        add.setOnClickListener(listener)
        sub.setOnClickListener(listener)
        mul.setOnClickListener(listener)
        div.setOnClickListener(listener)
        remainder.setOnClickListener(listener)

        result.setOnClickListener(listener2)
        del.setOnClickListener(listener2)

        val clear = findViewById<Button>(R.id.btn_clear)
        clear.setOnClickListener {
            etShow.setText("")
            etResult.setText("")
            d2 = 0.0
            d1 = d2
            number2 = ""
            number1 = number2
            history = number1
        }

        val plusMinus = findViewById<Button>(R.id.btn_plus_minus)
        plusMinus.setOnClickListener {
            if(etResult.text.toString() == "")
                Toast.makeText(this, "피연산되는 숫자가 입력되어야 합니다.", Toast.LENGTH_LONG).show()
            else
                etResult.setText((etResult.text.toString().toDouble() * -1).toString())
        }
    }

    @SuppressLint("SetTextI18n")
    fun onClick(v: View) {
        when (v.id) {
            R.id.btn0 -> etResult.setText(etResult.text.toString() + 0)
            R.id.btn1 -> etResult.setText(etResult.text.toString() + 1)
            R.id.btn2 -> etResult.setText(etResult.text.toString() + 2)
            R.id.btn3 -> etResult.setText(etResult.text.toString() + 3)
            R.id.btn4 -> etResult.setText(etResult.text.toString() + 4)
            R.id.btn5 -> etResult.setText(etResult.text.toString() + 5)
            R.id.btn6 -> etResult.setText(etResult.text.toString() + 6)
            R.id.btn7 -> etResult.setText(etResult.text.toString() + 7)
            R.id.btn8 -> etResult.setText(etResult.text.toString() + 8)
            R.id.btn9 -> etResult.setText(etResult.text.toString() + 9)
            R.id.btndot -> etResult.setText(etResult.text.toString() + ".")
        }
    }
}