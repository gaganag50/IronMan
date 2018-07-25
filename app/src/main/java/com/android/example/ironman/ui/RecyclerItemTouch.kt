package com.android.example.ironman.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by Neophyte Coder on 21/12/16.
 */
class RecyclerItemTouch(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {
    private val mGestureDetector: GestureDetector
    val TAG: String = "RecyclerItem"

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)


        fun onLongItemClick(view: View?, position: Int)
    }


    init {
        Log.d(TAG, ": on Create called ")

        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, ": onSingleTap called")

                return true
            }

            override fun onLongPress(e: MotionEvent) {

                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && mListener != null) {
                    Log.d(TAG, ": RIT called")

                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {

        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            Log.d(TAG, ": onInterceptTouchEvent called")

            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}