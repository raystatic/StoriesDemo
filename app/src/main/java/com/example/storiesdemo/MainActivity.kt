package com.example.storiesdemo

import android.graphics.Color
import android.graphics.Color.green
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poll_parent.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast
import toPixel

class MainActivity : AppCompatActivity(), MomentzCallback {

    private lateinit var pollView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // show a textview
        val textView = TextView(this)
        textView.text = "Hello, You can display TextViews"
        textView.textSize = 20f.toPixel(this).toFloat()
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.parseColor("#ffffff"))

        //show a customView
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_view, null)

        pollView = LayoutInflater.from(this).inflate(R.layout.poll_parent, null)



        // show an imageview be loaded from file
        val locallyLoadedImageView = ImageView(this)
        locallyLoadedImageView.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.dark
            )
        )

        //image to be loaded from the internet
        val internetLoadedImageView = ImageView(this)

        //video to be loaded from the internet
        val internetLoadedVideo = VideoView(this)

        val listOfViews = listOf(
//            MomentzView(textView, 5),
//            MomentzView(customView, 5),
//            MomentzView(locallyLoadedImageView, 6),
//            MomentzView(internetLoadedImageView, 10),
//            MomentzView(internetLoadedVideo, 60),
            MomentzView(pollView,60)
        )

        Momentz(this, listOfViews, container, this).start()

    }

    override fun done() {
        Toast.makeText(this@MainActivity, "Finished!", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
        if (view is VideoView) {
            momentz.pause(true)
            playVideo(view, index, momentz)
        } else if ((view is ImageView) && (view.drawable == null)) {
            momentz.pause(true)
            Picasso.get()
                .load("https://i.pinimg.com/564x/14/90/af/1490afa115fe062b12925c594d93a96c.jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view, object : Callback {
                    override fun onSuccess() {
                        momentz.resume()
                        Toast.makeText(this@MainActivity, "Image loaded from the internet", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(this@MainActivity,e?.localizedMessage, Toast.LENGTH_LONG).show()
                        e?.printStackTrace()
                    }
                })
        }else if(view.id == R.id.poll_view){

            pollView.tv_question.text = "What do you think? Is this positive or negative"

            val gestureDetector = GestureDetector(this, SingleTapConfirm())

            val touchListener = object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (gestureDetector.onTouchEvent(event)) {
                        // single tap
                        if(v?.id == R.id.left_rel){


                            toast("left")

                        } else if(v?.id == R.id.right_rel){

                            toast("right")
                        }
                        return true
                    }
                    return false
                }
            }

           pollView.left_card.setOnClickListener {
               updateButtons(pollView.left_card, pollView.right_card, pollView.left_rel, pollView.right_rel, pollView.tv_left, pollView.tv_right)
               pollView.tv_data_left.visibility = View.VISIBLE
               pollView.tv_data_right.visibility = View.GONE
           }

            pollView.right_card.setOnClickListener {
                updateButtons(pollView.right_card,pollView.left_card, pollView.right_rel, pollView.left_rel, pollView.tv_right, pollView.tv_left)
                pollView.tv_data_left.visibility = View.GONE
                pollView.tv_data_right.visibility = View.VISIBLE
            }

        }
    }

    private fun updateButtons(activeButton: View?, inActiveButton: View?, activeRel:RelativeLayout, inActiveRel:RelativeLayout,activeText:TextView, inActiveText:TextView) {
        activeButton?.elevation = 0f
        inActiveButton?.elevation = 100f

        activeRel.background = resources.getDrawable(R.color.green)
        inActiveRel.background = resources.getDrawable(R.color.white)

        activeText.textSize = 12f
        inActiveText.textSize = 16f

//        ViewCompat.setBackgroundTintList(activeButton as View, ContextCompat.getColorStateList(this, R.color.grey))
//        ViewCompat.setBackgroundTintList(inActiveButton as View, ContextCompat.getColorStateList(this, R.color.white))

    }

    private fun playVideo(videoView: VideoView, index: Int, momentz: Momentz) {
        val str = "https://images.all-free-download.com/footage_preview/mp4/triumphal_arch_paris_traffic_cars_326.mp4"
        val uri = Uri.parse(str)

        videoView.setVideoURI(uri)

        videoView.requestFocus()
        videoView.start()

        videoView.setOnInfoListener(object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // Here the video starts
                    momentz.editDurationAndResume(index, (videoView.duration) / 1000)
                    Toast.makeText(this@MainActivity, "Video loaded from the internet", Toast.LENGTH_LONG).show()
                    return true
                }
                return false
            }
        })
    }


    private inner class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }

}
