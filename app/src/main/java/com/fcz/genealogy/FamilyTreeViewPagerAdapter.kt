package com.fcz.genealogy.ui.familytree

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fcz.genealogy.ui.familytree.filter.FilterViewFragment
import com.fcz.genealogy.ui.familytree.generation.GenerationViewFragment
import com.fcz.genealogy.ui.familytree.list.ListViewFragment
import com.fcz.genealogy.ui.familytree.tree.TreeViewFragment

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