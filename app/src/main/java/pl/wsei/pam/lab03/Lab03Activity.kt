package pl.wsei.pam.lab03

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {

    lateinit var size: IntArray;
    lateinit var mBoard: GridLayout;
    lateinit var mBoardModel: MemoryBoardView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab03)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lab03)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBoard = findViewById(R.id.lab03)
        size = intent.getIntArrayExtra("size") ?: intArrayOf(3,3);
        mBoard.columnCount = size?.first() ?: 10
        mBoard.rowCount = size?.last() ?: 10

        val a =  mBoard.columnCount
        val b =  mBoard.rowCount

        mBoardModel = MemoryBoardView(mBoard, mBoard.columnCount, mBoard.columnCount)

        if (savedInstanceState != null) {

            val imagesFromBundle = savedInstanceState.getIntArray("Images");
            val revealedFromBundle = savedInstanceState.getBooleanArray("Revealed");
            val tagsFromBundle = savedInstanceState.getStringArray("Tag");


            if(imagesFromBundle != null && revealedFromBundle != null && tagsFromBundle != null){
                mBoardModel.setState(imagesFromBundle, revealedFromBundle, tagsFromBundle);
            }
        }



        mBoardModel.setOnGameChangeListener { e ->
            run {
                when (e.state) {
                    GameStates.Matching -> {
                        e.tiles.forEach{it.revealed = true}
                    }
                    GameStates.Match -> {
                        e.tiles.forEach{it.revealed = true}
                    }
                    GameStates.NoMatch -> {
                        e.tiles.forEach{it.revealed = true}
                        Timer().schedule(2000) {
                            // kod wykonany po 2000 ms
                            runOnUiThread(){
                                e.tiles.forEach{it.revealed = false}
                            }

                        }
                    }
                    GameStates.Finished -> {
                        Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState);
//
//        outState.putIntArray("Images", mBoardModel.getState().map { it.id }.toIntArray());
//        outState.putBooleanArray(
//            "Revealed",
//            mBoardModel.getState().map { it.revealed }.toBooleanArray()
//        );
//        outState.putStringArray("Tag", mBoardModel.getState().map { it.tag }.toTypedArray());
//    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putIntArray("Images", mBoardModel.getState().map { it.id }.toIntArray());
        outState.putBooleanArray(
            "Revealed",
            mBoardModel.getState().map { it.revealed }.toBooleanArray()
        );
        outState.putStringArray("Tag", mBoardModel.getState().map { it.tag }.toTypedArray());
    }
}