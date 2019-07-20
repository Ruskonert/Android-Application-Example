package kr.cnehs.seoya.application

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.lang.NumberFormatException

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate", "PropertyName")
class SecondActivity : AppCompatActivity() {
    enum class Operator { ADD, SUB, MUL, DIV, REMINDER }

    val et_show   = findViewById<EditText>(R.id.et_show)
    val et_result = findViewById<EditText>(R.id.et_result)
    val add = findViewById<Button>(R.id.btn_add)
    val sub = findViewById<Button>(R.id.btn_sub)
    val mul = findViewById<Button>(R.id.btn_mul)
    val div = findViewById<Button>(R.id.btn_div)

    val remainder = findViewById<Button>(R.id.btn_remainder)
    val del       = findViewById<Button>(R.id.btn_del)
    val result    = findViewById<Button>(R.id.btn_result)

    //이번 연산의 결과를 저장합니다.
    var history = ""

    // 피연산자1
    var number1 = ""

    // 피연산자2
    var number2 = ""

    // 어떤 연산자가 선택되었는지 확인하기 위한 변수입니다.
    // Operator 클래스를 참고하세요.
    var operationType = Operator.ADD

    var operator1 : Double = 0.0
    var operator2 : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // 지우는 버튼에 대해서 클릭 이벤트를 지정합니다.
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            et_show.setText(""); et_result.setText("")
            operator2 = 0.0; operator1 = 0.0
            number2 = ""; number1 = number2
            history = number1
        }

        // 부호 바꾸는 버튼에 대해서 클릭 이벤트를 지정합니다.
        findViewById<Button>(R.id.btn_plus_minus).setOnClickListener {
            //실수인지 정수인지 판단해서 부호 바꿉니다.
            try {
                et_result.text.toString().toDouble()
                et_result.setText(et_result.text.toString().toInt() * -1)
            }
            catch(_ : NumberFormatException) {
                et_result.setText((et_result.text.toString().toDouble() * -1).toString())
            }
        }

        // 나머지 버튼에 대해서 클릭 이벤트를 지정합니다.
        // 클릭되었을 때, 실행되는 함수는 mListener 변수의 루틴입니다.
        add.setOnClickListener(mListener)
        sub.setOnClickListener(mListener)
        mul.setOnClickListener(mListener)
        div.setOnClickListener(mListener)
        remainder.setOnClickListener(mListener)
        result.setOnClickListener(mListener)
        del.setOnClickListener(mListener)
    }

     val mListener : View.OnClickListener = View.OnClickListener {
         if (et_result.text.toString() == "") {
             Toast.makeText(this@SecondActivity, "수를 입력하세요", Toast.LENGTH_SHORT).show()
             return@OnClickListener
         }
         when(it.id) {
             R.id.btn_add -> {
                 number1 = et_result.text.toString()
                 history = et_result.text.toString() + " + "
                 et_show.setText(history)
                 et_result.setText("")
                 operationType = Operator.ADD
             }
             R.id.btn_sub -> {
                 number1 = et_result.text.toString()
                 history = et_result.text.toString() + " - "
                 et_show.setText(history)
                 et_result.setText("")
                 operationType = Operator.SUB
             }
             R.id.btn_mul -> {
                 number1 = et_result.text.toString()
                 history = et_result.text.toString() + " * "
                 et_show.setText(history)
                 et_result.setText("")
                 operationType = Operator.MUL
             }
             R.id.btn_div -> {
                 number1 = et_result.text.toString()
                 history = et_result.text.toString() + " / "
                 et_show.setText(history)
                 et_result.setText("")
                 operationType = Operator.DIV
             }
             R.id.btn_remainder -> {
                 number1 = et_result.text.toString()
                 history = et_result.text.toString() + " % "
                 et_show.setText(history)
                 et_result.setText("")
                 operationType = Operator.REMINDER
             }
             R.id.btn_del -> {
                 val deleteNumber = et_result.text.toString()
                 Toast.makeText(this@SecondActivity, deleteNumber, Toast.LENGTH_SHORT).show()
                 et_result.setText(deleteNumber . substring (0, deleteNumber.length - 1))
             }
             R.id.btn_result -> {
                 try {
                     operator1 = java.lang.Double.parseDouble(number1)
                     operator2 = java.lang.Double.parseDouble(number2)
                 }
                 catch(_ : NumberFormatException) {
                     Toast.makeText(this@SecondActivity, "숫자가 비어있습니다!", Toast.LENGTH_SHORT).show()
                     return@OnClickListener
                 }

                 val result : Double
                 Toast.makeText(this@SecondActivity, "결과", Toast.LENGTH_SHORT).show()
                 number2 = et_result.text.toString()
                 history += et_result.text.toString()
                 et_show.setText(history)
                 when (operationType) {
                     Operator.ADD -> {
                         result = operator1 + operator2
                         et_result.setText(result.toString())
                     }
                     Operator.SUB -> {
                         result = operator1 - operator2
                         et_result.setText(result.toString())
                     }
                     Operator.MUL -> {
                         result = operator1 * operator2
                         et_result.setText(result.toString())
                     }
                     Operator.DIV -> {
                         result = operator1 / operator2
                         et_result.setText(result.toString())
                     }
                     Operator.REMINDER -> {
                         result = operator1 % operator2
                         et_result.setText(result.toString())
                     }
                 }
             }
         }
     }

    /**
     * 숫자 버튼이 입력되었을 때, 발생하는 이벤트 함수입니다.
     */
    @SuppressLint("SetTextI18n")
    fun onClick(view : View) {
        when (view.id) {
            R.id.btn0 -> et_result.setText(et_result.text.toString() + 0)
            R.id.btn1 -> et_result.setText(et_result.text.toString() + 1)
            R.id.btn2 -> et_result.setText(et_result.text.toString() + 2)
            R.id.btn3 -> et_result.setText(et_result.text.toString() + 3)
            R.id.btn4 -> et_result.setText(et_result.text.toString() + 4)
            R.id.btn5 -> et_result.setText(et_result.text.toString() + 5)
            R.id.btn6 -> et_result.setText(et_result.text.toString() + 6)
            R.id.btn7 -> et_result.setText(et_result.text.toString() + 7)
            R.id.btn8 -> et_result.setText(et_result.text.toString() + 8)
            R.id.btn9 -> et_result.setText(et_result.text.toString() + 9)
            R.id.btndot -> et_result.setText(et_result.text.toString() + ".")
        }
    }
}
