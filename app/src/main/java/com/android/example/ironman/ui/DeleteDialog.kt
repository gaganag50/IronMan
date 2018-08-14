package com.android.example.ironman.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.util.Log
import com.android.example.ironman.db.Expense
import io.objectbox.Box
import java.lang.ref.WeakReference

class DeleteDialog(


) {


    companion object {
        fun ShowMsgDialog(self: Context, title: String, Msg: String, mHandler: Handler, item: Expense, expenseBox: Box<Expense>?) {

            val dlgAlert = AlertDialog.Builder(self)
            dlgAlert.setTitle(title)
            dlgAlert.setMessage(Msg)
            dlgAlert.setPositiveButton("OK") { dialog, whichButton ->
                // call your code here
                expenseBox!!.remove(item)

                Log.d(TAG, ": ShowMsDialog is called")


                val msg = Message.obtain()

                msg.arg1 = HandlerMessage.Yes.ordinal
                mHandler.sendMessage(msg)
            }
            dlgAlert.setNegativeButton("Cancel") { dialog, which ->
                // TODO Auto-generated method stub
                val msg = Message.obtain()
                msg.arg1 = HandlerMessage.No.ordinal
                mHandler.sendMessage(msg)
            }
            dlgAlert.show()
        }

        internal enum class HandlerMessage {
            Yes,
            No
        }


        class IncomingHandler(activity: MainActivity) : Handler() {
            private val mActivityWeakReference: WeakReference<MainActivity>

            init {
                mActivityWeakReference = WeakReference(activity)
            }

            override fun handleMessage(msg: Message) {
                val activity = mActivityWeakReference.get()
                activity?.HandleMessage(msg)
            }
        }
    }


}