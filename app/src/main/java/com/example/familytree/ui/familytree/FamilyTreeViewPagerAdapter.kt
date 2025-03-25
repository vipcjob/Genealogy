package com.example.familytree.ui.familytree

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familytree.ui.familytree.filter.FilterViewFragment
import com.example.familytree.ui.familytree.generation.GenerationViewFragment
import com.example.familytree.ui.familytree.list.ListViewFragment
import com.example.familytree.ui.familytree.tree.TreeViewFragment

class FamilyTreeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    
    override fun getItemCount(): Int = 4
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TreeViewFragment()
            1 -> ListViewFragment()
            2 -> GenerationViewFragment()
            3 -> FilterViewFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
} 