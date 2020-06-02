package com.zphinx.filterdialog.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zphinx.filterdialog.FilterGroup
import com.zphinx.filterdialog.R


class FilterAdapter(val activity: AppCompatActivity, val filterList: List<FilterGroup>, onFilterApplied: (ArrayList<FilterGroup>) -> Unit) :
    BaseExpandableListAdapter() {


    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    override fun getGroup(groupPosition: Int): FilterGroup {

        return filterList[groupPosition]
    }

    /**
     * Whether the child at the specified position is selectable.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group
     * @return whether the child is selectable.
     */
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the
     * underlying data.
     *
     * @return whether or not the same ID always refers to the same object
     * @see Adapter.hasStableIds
     */
    override fun hasStableIds(): Boolean {
        return true
    }

    /**
     * Gets a View that displays the given group. This View is only for the
     * group--the Views for the group's children will be fetched using
     * [.getChildView].
     *
     * @param groupPosition the position of the group for which the View is
     * returned
     * @param isExpanded whether the group is expanded or collapsed
     * @param convertView the old view to reuse, if possible. You should check
     * that this view is non-null and of an appropriate type before
     * using. If it is not possible to convert this view to display
     * the correct data, this method can create a new view. It is not
     * guaranteed that the convertView will have been previously
     * created by
     * [.getGroupView].
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val listTitle = getGroup(groupPosition).groupName
        val currentView = when (convertView) {
            null -> {
                val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                layoutInflater.inflate(R.layout.filter_group, null)
            }
            else -> {
                convertView
            }
        }

        val listTitleTextView = currentView.findViewById(R.id.filter_group_item) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        val doEpxand = getGroup(groupPosition).isExpanded
        if(doEpxand){
            listTitleTextView.performClick()
        }

        return currentView!!
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     * count should be returned
     * @return the children count in the specified group
     */
    override fun getChildrenCount(groupPosition: Int): Int {
          return filterList[groupPosition].groupChildren.size
    }

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     * children in the group
     * @return the data of the child
     */
    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return filterList[groupPosition].groupChildren[childPosition].name
    }

    /**
     * Gets the ID for the group at the given position. This group ID must be
     * unique across groups. The combined ID (see
     * [.getCombinedGroupId]) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    override fun getGroupId(groupPosition: Int): Long {
        return filterList[groupPosition].id
    }
    override fun onGroupCollapsed(groupPosition: Int) {}

    override fun onGroupExpanded(groupPosition: Int) {

    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     * returned) within the group
     * @param isLastChild Whether the child is the last child within the group
     * @param convertView the old view to reuse, if possible. You should check
     * that this view is non-null and of an appropriate type before
     * using. If it is not possible to convert this view to display
     * the correct data, this method can create a new view. It is not
     * guaranteed that the convertView will have been previously
     * created by
     * [.getChildView].
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val currentView = when (convertView) {
            null -> {
                val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                layoutInflater.inflate(R.layout.filter_item, null)
            }
            else -> {
                convertView
            }
        }

        val currentChild = filterList[groupPosition].groupChildren[childPosition]
        val childCheckbox = currentView.findViewById(R.id.filter_child) as CheckBox
        childCheckbox.setTypeface(null, Typeface.NORMAL)
        childCheckbox.text = currentChild.name
        childCheckbox.setOnCheckedChangeListener{view,childChecked->
            currentChild.isChecked = childChecked
        }
        childCheckbox.isChecked = currentChild.isChecked
        return currentView!!
    }

    /**
     * Gets the ID for the given child within the given group. This ID must be
     * unique across all children within the group. The combined ID (see
     * [.getCombinedChildId]) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group for which
     * the ID is wanted
     * @return the ID associated with the child
     */
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return filterList[groupPosition].groupChildren[childPosition].id
    }

    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    override fun getGroupCount(): Int {

        return filterList.size
    }

    fun updateChildView(groupPosition: Int, childPosition: Int,checked:Boolean) {
        Log.d(TAG,"Child view updated with grp pos: $groupPosition and childPosition: $childPosition and checked value: $checked")
        filterList[groupPosition].groupChildren[childPosition].isChecked = checked
    }


}
