package com.example.storiesdemo

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.*

class PollView : AnkoComponent<ViewGroup>  {

    lateinit var centerRel:RelativeLayout
    lateinit var leftRel:RelativeLayout
    lateinit var rightRel:RelativeLayout
    lateinit var rightTv:TextView
    lateinit var leftTv:TextView
    lateinit var root:CardView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui){
        relativeLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            root = cardView {
                radius = 16f
                cardElevation = 8f
                relativeLayout {
                    id = View.generateViewId()

                    centerRel = relativeLayout {
                        background = this.resources.getDrawable(R.color.black)
                        id = View.generateViewId()
                    }.lparams(width = dip(1), height = matchParent) {
                        centerInParent()
                    }

                    leftRel = relativeLayout {
                        background = this.resources.getDrawable(R.color.white)
                        id = View.generateViewId()

                        leftTv =  textView {
                            text = "POSITIVE"
                            id = View.generateViewId()
                            textSize = 16f
                            textColor = context.resources.getColor(R.color.black)
                        }.lparams(width = wrapContent, height = wrapContent) {
                            centerInParent()
                        }

                        setOnClickListener {
                            toast("${leftTv.text}")
                        }

                    }.lparams(width = matchParent, height = matchParent) {
                        leftOf(centerRel.id)
                    }

                    rightRel = relativeLayout {
                        background = this.resources.getDrawable(R.color.white)
                        id = View.generateViewId()

                        rightTv =  textView {
                            text = "NEGATIVE"
                            id = View.generateViewId()
                            textSize = 16f
                            textColor = context.resources.getColor(R.color.black)
                        }.lparams(width = wrapContent, height = wrapContent) {
                            centerInParent()
                        }

                        setOnClickListener {
                            toast("${rightTv.text}")
                        }

                    }.lparams(width = matchParent, height = matchParent) {
                        rightOf(centerRel.id)
                    }

                }.lparams(width = matchParent, height = matchParent) {

                }
            }.lparams(width = matchParent, height = dip(50)) {

            }
        }
    }
}