package pl.wsei.pam.lab03

import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import java.util.Stack

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.baseline_deck_24,
        R.drawable.baseline_rocket_launch_24,
        R.drawable.baseline_ac_unit_24,
        R.drawable.baseline_access_alarms_24,
        R.drawable.baseline_access_time_filled_24,
        R.drawable.baseline_accessibility_24,
        R.drawable.baseline_accessible_forward_24,
        R.drawable.baseline_add_box_24,
        R.drawable.baseline_add_box_24,
        R.drawable.baseline_add_business_24,
        R.drawable.baseline_add_call_24,
        R.drawable.baseline_add_card_24,
        R.drawable.baseline_add_chart_24,
        R.drawable.baseline_add_shopping_cart_24,
        R.drawable.baseline_add_task_24,
        R.drawable.baseline_add_to_drive_24,
        R.drawable.baseline_add_to_home_screen_24,
        R.drawable.baseline_add_to_photos_24,
        R.drawable.baseline_add_to_queue_24,
        R.drawable.baseline_audiotrack_24
        // dodaj kolejne identyfikatory utworzonych ikon
    )
    init {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(icons.subList(0, cols * rows / 2))
            it.addAll(icons.subList(0, cols * rows / 2))
            it.shuffle()
        }



        for (row in 0..gridLayout.rowCount-1){
            for(col in 0..gridLayout.columnCount-1){
                val btn = ImageButton(gridLayout.context).also {
                    it.tag = "${row}x${col}"
//                    it.setImageResource(shuffledIcons.get(row+col))
                    it.setImageResource(R.drawable.baseline_deck_24)
                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    it.layoutParams = layoutParams
                    gridLayout.addView(it)
                }
                addTile(btn, shuffledIcons.get(row+col))
            }
        }
        // tu umieść kod pętli tworzący wszystkie karty, który jest obecnie
        // w aktywności Lab03Activity
    }
    private val deckResource: Int = R.drawable.baseline_deck_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag]
        matchedPair.push(tile)
        val matchResult = logic.process {
            tile?.tileResource?:-1
        }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
}