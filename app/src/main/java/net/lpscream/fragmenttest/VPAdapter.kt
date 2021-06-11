package net.lpscream.fragmenttest

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VPAdapter(activity: AppCompatActivity, var itemsCount: Int) : FragmentStateAdapter(activity) {



    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentPage.getInstance(position)
    }

    fun setItemCount(count: Int){
        itemsCount = count
        notifyDataSetChanged()
    }

    fun removeItem(count: Int){
        itemsCount = count
        notifyItemRemoved(count)
    }

}