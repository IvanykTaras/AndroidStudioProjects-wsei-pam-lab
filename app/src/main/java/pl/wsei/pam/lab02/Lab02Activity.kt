package pl.wsei.pam.lab02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab02)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lab02)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonFirst: Button = findViewById(R.id.main_6_6_board)
        val buttonSecond: Button = findViewById(R.id.main_4_4_board)
        val buttonThird: Button = findViewById(R.id.main_4_3_board)
        val buttonFourth: Button = findViewById(R.id.main_3_2_board)

        buttonFirst.setOnClickListener {
            buttonEvent(intArrayOf(6,6))
        }
        buttonSecond.setOnClickListener {
            buttonEvent(intArrayOf(4,4))
        }
        buttonThird.setOnClickListener {
            buttonEvent(intArrayOf(4,3))
        }
        buttonFourth.setOnClickListener {
            buttonEvent(intArrayOf(3,2))
        }

    }

    private fun buttonEvent(size: IntArray){
        val intent = Intent(this, Lab03Activity::class.java)
        intent.putExtra("size",size)
        startActivity(intent)
    }

}