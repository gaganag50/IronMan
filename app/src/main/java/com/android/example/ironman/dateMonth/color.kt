package com.android.example.ironman.dateMonth

import android.support.v4.app.FragmentManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.android.example.ironman.ui.EditActivity
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener

class showColorPickerDialog {

    companion object {
        fun showColorPickerDialog(
                context: EditActivity,
                color_mode_spinner: Spinner,
                mColorTextView: TextView,
                fragmentManager: FragmentManager,
                mColor: Int,
                colorMode: ColorMode
        ) {

            var dialogColor = mColor


            val adapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_dropdown_item, ColorMode.values())
            color_mode_spinner.setAdapter(adapter)
            color_mode_spinner.setSelection(adapter.getPosition(colorMode))


            ChromaDialog.Builder()
                    .initialColor(dialogColor)
                    .colorMode(color_mode_spinner.getSelectedItem() as ColorMode)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            updateTextView(color)
                            dialogColor = color
                        }

                        private fun updateTextView(color: Int) {
                            mColorTextView.setText(String.format("#%06X", 0xFFFFFF and color))

                        }
                    })
                    .create()

                    .show(fragmentManager, "dialog")


        }
    }
}