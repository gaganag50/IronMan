package com.android.example.ironman

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.buttons.*
import java.util.*
import android.text.InputType
import android.os.Build
import android.widget.EditText



class Calculator : AppCompatActivity() {

    private val calculations = ArrayList<CalculationLog>()
    // an OnTouchListener for the edit text that does absolutely nothing (absorbs the touch)
    private val otl = View.OnTouchListener { v, event ->
        true // the listener has consumed the event
    }

    private var mAdapter: RecyclerView.Adapter<*>? = null
    /**
     * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
     * @param editText
     */




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calculator)






        editText.requestFocus()
        editText.setOnTouchListener(otl)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        my_recycler_view.setHasFixedSize(true)

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(this)
        my_recycler_view.layoutManager = mLayoutManager

        // specify an adapter (see also next example)
        mAdapter = RecyclerViewAdapter(calculations)
        my_recycler_view.adapter = mAdapter

        button_del.setOnClickListener { editText.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) }

        // Delete button long press
        button_del.setOnLongClickListener {
            editText.setText(null)
            true
        }

        // Left button long press
        button_left.setOnLongClickListener {
            editText.setSelection(0, 0)
            true
        }

        // Right button long press
        button_right.setOnLongClickListener {
            val textLength = editText.text.length
            editText.setSelection(textLength, textLength)
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun calculateResult(view: View) {

        //String input = editText.getEditText().getText().toString();
        val input = editText.text.toString()
        if (input.isEmpty()) {
            return
        }

        // Do the calculation
        try {
            doResult(input)
            editText.setText(null)
        } catch (e: Exception) {
            if (!e.message!!.isEmpty()) {
                val message = e.message
                //        editText.setError(message);
                val snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {
                val error = e as CharSequence
                //        editText.setError(error);
                val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

    }

    @Throws(Exception::class)
    private fun doResult(input: String) {

        val result = Maths.doMath(input, this)
        val c = CalculationLog(input, result!!)
        val calcSize = calculations.size
        calculations.add(c)
        mAdapter!!.notifyItemInserted(calcSize)
        my_recycler_view.scrollToPosition(calcSize)
    }

    fun buttonPressed(view: View) {
        val b = view as Button
        val buttonText = b.text
        editText.text.insert(editText.selectionStart, buttonText)
    }

//    public void delPressed(@SuppressWarnings("UnusedParameters") View view) {
//        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//    }
//
//    public void delClear(View view) {
//        editText.setText(null);
//    }

    fun rightPressed(view: View) {
        editText.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT))
    }

    fun leftPressed(view: View) {
        editText.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT))
    }

    fun trigButtonPressed(view: View) {
        val myMenu = PopupMenu(this, view)
        myMenu.inflate(R.menu.menu_trig)

        view.setOnTouchListener(myMenu.dragToOpenListener)
        // Define a click listener
        myMenu.setOnMenuItemClickListener { item ->
            editText.text.insert(editText.selectionStart, item.title)
            true
        }

        myMenu.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.action_about -> return true
            R.id.action_clear_hist -> {
                calculations.clear()
                mAdapter!!.notifyDataSetChanged()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

//    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        val result = super.onPrepareOptionsMenu(menu)
//        menu.findItem(R.id.action_about).setOnMenuItemClickListener {
//            startActivity(Intent(this, AboutActivity::class.java))
//            true
//        }
//        return result
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}
