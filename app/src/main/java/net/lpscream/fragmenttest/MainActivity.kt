package net.lpscream.fragmenttest

import android.R.attr.radius
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity_log"

    private lateinit var textView: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var vpAdapter: VPAdapter

    private var itemCount: Int = 1

    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        viewPager = findViewById(R.id.viewPager)
        btnMinus = findViewById(R.id.btn_minus)
        btnMinus.visibility = View.INVISIBLE
        btnPlus = findViewById(R.id.btn_plus)
        vpAdapter = VPAdapter(this, itemCount)
        viewPager.adapter = vpAdapter
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
        textView = findViewById(R.id.fragment_number)
        createRoundedCorners(textView, R.color.btn_background)
        createRoundedCorners(btnMinus, R.color.white)
        createRoundedCorners(btnPlus, R.color.white)
        textView.text = (viewPager.currentItem + 1).toString()
        plusFragment()
        minusFragment()
        getValueFromIntent()
        Log.d(TAG, "onCreate: getItemCount() = " + getItemCount())
    }


    private fun getItemCount(): Int {
        val sharedPreferences =
            getSharedPreferences("net.lpscream.fragmenttest", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("itemCount", 1)
    }

    private fun saveItemCount() {
        val sharedPreferences =
            getSharedPreferences("net.lpscream.fragmenttest", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("itemCount", itemCount)
        Log.d(TAG, "saveItemCount: itemCount " + itemCount)
        editor.apply()
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent?.getStringExtra("menuNumber") != null) {
            itemCount = intent.getStringExtra("menuNumber")?.toInt()!! + 1
            vpAdapter.itemCount = intent.getStringExtra("menuNumber")?.toInt()!! + 1
            viewPager.currentItem = intent.getStringExtra("menuNumber")?.toInt()!!
        }
        super.onNewIntent(intent)
    }

    private var pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            textView.text = (position + 1).toString()
        }
    }

    fun createRoundedCorners(view: View, color: Int) {
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius.toFloat())
            .build()
        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this, color))
        shapeDrawable.setStroke(2.0f, ContextCompat.getColor(this, color))
        ViewCompat.setBackground(view, shapeDrawable)
    }

    fun plusFragment() {
        btnPlus.setOnClickListener {
            if (itemCount >= 1) {
                btnMinus.visibility = View.VISIBLE
                itemCount = itemCount + 1
                vpAdapter.itemCount = itemCount
                viewPager.currentItem = itemCount - 1
            }
        }
    }

    fun minusFragment() {
        btnMinus.setOnClickListener {
            if (itemCount == 1) {
                btnMinus.visibility = View.INVISIBLE
            } else {
                itemCount = itemCount - 1
                vpAdapter.removeItem(itemCount)
                viewPager.currentItem = itemCount
            }
        }
        notificationManager.cancel(itemCount - 1)
    }

    fun getValueFromIntent() {
        if (vpAdapter.itemCount == 1 && intent.getStringExtra("menuNumber") != null) {
            itemCount = intent.getStringExtra("menuNumber")?.toInt()!! + 1
            vpAdapter.itemCount = intent.getStringExtra("menuNumber")?.toInt()!! + 1
            viewPager.currentItem = intent.getStringExtra("menuNumber")?.toInt()!!
        } else {
            if (intent.getStringExtra("menuNumber") != null) {
                viewPager.currentItem = intent.getStringExtra("menuNumber")?.toInt()!!
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveItemCount()
    }
}