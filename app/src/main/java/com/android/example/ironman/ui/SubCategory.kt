//package com.android.example.ironman.ui
//
//
//import android.content.Context
//import android.icu.text.IDNA
//import android.widget.ImageView
//import android.widget.TextView
//import com.android.example.ironman.R
//import com.mindorks.placeholderview.annotations.Layout
//import com.mindorks.placeholderview.annotations.Resolve
//import com.mindorks.placeholderview.annotations.View
//import com.mindorks.placeholderview.annotations.expand.ChildPosition
//import com.mindorks.placeholderview.annotations.expand.ParentPosition
//
//@Layout(R.layout.activity_subcategory)
//class InfoView(private val mContext: Context, private val mInfo: IDNA.Info) {
//    @ParentPosition
//    private val mParentPosition: Int = 0
//
//    @ChildPosition
//    private val mChildPosition: Int = 0
//
//    @View(R.id.titleTxt)
//    private val titleTxt: TextView? = null
//
//    @View(R.id.captionTxt)
//    private val captionTxt: TextView? = null
//
//    @View(R.id.timeTxt)
//    private val timeTxt: TextView? = null
//
//    @View(R.id.imageView)
//    private val imageView: ImageView? = null
//
//    @Resolve
//    private fun onResolved() {
//        titleTxt!!.setText(mInfo.getTitle())
//        captionTxt!!.setText(mInfo.getCaption())
//        timeTxt!!.setText(mInfo.getTime())
////        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView)
//    }
//
//}
