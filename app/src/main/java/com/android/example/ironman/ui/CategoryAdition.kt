package com.android.example.ironman.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.example.ironman.R
import com.android.example.ironman.adapter.CategoryAdapter
import com.android.example.ironman.adapter.PurchaseItemRecyclerViewAdapter
import com.android.example.ironman.data.AndroidCategoryAssets
import com.android.example.ironman.db.Category
import com.android.example.ironman.ui.MainActivity.Companion.boxStore
import org.json.JSONArray
import org.json.JSONObject


class CategoryAddition : AppCompatActivity() {
    val TAG: String = "CategoryAdd"
    lateinit var list: ArrayList<Category>
    lateinit var adapter: CategoryAdapter
    val image = R.drawable.ic_launcher_background

    companion object {


        var categoryBox = boxStore?.boxFor(Category::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_category)

        val drawable = resources.getDrawable(image)




        Log.d(TAG, ": onCreateCalled")
        val count = categoryBox!!.count()
        if (count == 0L) {
            saveExistingCategoriesInDatabase()


        }

        list = ArrayList(categoryBox!!.all)

        val recyclerView = findViewById<RecyclerView>(R.id.expandableView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val testAdapter = PurchaseItemRecyclerViewAdapter(this, list)
//        recyclerView.adapter = testAdapter


//        btnSaveCategory.setOnClickListener {
//            Log.d(TAG, ": btnSaveCategory called")
//
//            startActivity(Intent(this@CategoryAddition, AddCategory::class.java))
//        }
//
//
    }

    fun saveExistingCategoriesInDatabase() {
        val list = AndroidCategoryAssets.getCategories()
        val listOfSubCategory = AndroidCategoryAssets.getfood()
        Log.d(TAG, ": listOfSubCategory ${listOfSubCategory.toList()}")

        val arrayList: ArrayList<String> = ArrayList()

        for (i in 0 until listOfSubCategory.size) {
            arrayList.add(resources.getString(listOfSubCategory[i]))
            Log.d(TAG, ": listOfSubCategory ${resources.getString(listOfSubCategory[i])}")

        }
        val json = JSONObject()
        json.put("uniqueArrays", JSONArray(arrayList))
        val aList = json.toString()

        for (i in 0 until list.size) {
            val cat = Category(
                    resources.getString(list[i]), aList


            )
            Log.d(TAG, ": arrayList ${arrayList.toList()}")
            categoryBox!!.put(cat)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        refreshList()
//
//    }
//
//    private fun refreshList() {
//        val newList = SugarRecord.listAll(Category::class.java).toList()
//
//        list.clear()
//        list.addAll(ArrayList(newList))
//        Log.d(TAG, ": newList ${list.toList()}")
//
//        adapter.notifyDataSetChanged()
//
//    }


}
