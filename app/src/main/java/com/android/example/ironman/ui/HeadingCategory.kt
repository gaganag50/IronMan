package com.android.example.ironman.ui


import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.example.ironman.R
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.expand.*


//@Parent is annotation used to bind a class as the parent view.
//@layout is used to bind the layout with this class
//@SingleTop is used to keep only one parent in expanded state and others in collapsed
// state.
//@View is used to bind the views in a layout we want to refer to

//@Toggle is used to provide a view in the layout to be used as a toggle for
// expanding or collapsing a parent on view click. If not provided then the parent
// view is used as a toggle view by default.

//@ParentPosition is used bind an int variable to be updated with the relative
// position of a parent with respect to other parents.

//@Expand is used to get a method invoked when the parent view expands.
//@Collapse is used to get a method invoked when the parent view collapses.

@Parent
@SingleTop
@Layout(R.layout.activity_category)
class HeadingView(private val mContext: Context, private val mHeading: String) {

    @View(R.id.headingTxt)
    private val headingTxt: TextView? = null

    @View(R.id.toggleIcon)
    private val toggleIcon: ImageView? = null

    @Toggle(R.id.toggleView)
    private val toggleView: LinearLayout? = null

    @ParentPosition
    private val mParentPosition: Int = 0

    @Resolve
    private fun onResolved() {
        toggleIcon!!.setImageDrawable(mContext.resources.getDrawable(R.drawable.baseline_expand_less_black_18dp))
        headingTxt!!.text = mHeading
    }

    @Expand
    private fun onExpand() {
        toggleIcon!!.setImageDrawable(mContext.resources.getDrawable(R.drawable.baseline_expand_more_black_18dp))
    }

    @Collapse
    private fun onCollapse() {
        toggleIcon!!.setImageDrawable(mContext.resources.getDrawable(R.drawable.baseline_expand_less_black_18dp))
    }
}
