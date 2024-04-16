package pl.wsei.pam.lab03

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import java.util.Random
import java.util.Stack

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
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

    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag]
        if (tile != null) {
            if(matchedPair.count() != 0) {
                if (tile.button.tag == matchedPair.get(0).button.tag) {
                    return;
                }
            }
            matchedPair.push(tile)
        }

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
        val deckResource: Int = R.drawable.baseline_deck_24

        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }

    fun getState(): List<TileState> {
        val output: MutableList<TileState> = mutableListOf();

        tiles.forEach {
            var newTileState = TileState(it.value.tileResource, it.value.revealed, it.key);
            output.add(newTileState);
        }

        return output;
    }

    fun setState(imagesArray: IntArray, revealedArray: BooleanArray, tagsArray: Array<String>) {
        var index: Int = 0;
        tiles.forEach{
            it.value.tileResource = imagesArray[index];
            it.value.revealed = revealedArray[index];

            index++;
        }
    }

    fun animatePairedButton(button: ImageButton, action: Runnable ) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)
        set.startDelay = 500
        set.duration = 2000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scallingX, scallingY, fade)
        set.addListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.isEnabled = false;
                button.alpha = 0.0f
                action.run();
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        set.start()
    }

    fun animateNoPairedButton(button: ImageButton, action: Runnable ) {
        val set = AnimatorSet()
        val random = Random()


        val rotationLeft = ObjectAnimator.ofFloat(button, "rotation", -25f)
        val rotationRight = ObjectAnimator.ofFloat(button, "rotation", 25f)
        val rotationRight2 = ObjectAnimator.ofFloat(button, "rotation", 25f)

        set.startDelay = 0
        set.duration = 100
        set.interpolator = DecelerateInterpolator()

        set.playSequentially(rotationRight,rotationLeft,rotationRight2)
//        set.playTogether(rotationLeft,rotationRight)

        set.addListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                button.rotation = 25f;
                button.isEnabled = false;
                action.run();
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        set.start()
    }


}

data class TileState(
    val id: Int, //obrazki
    val revealed: Boolean,
    val tag: String
){}