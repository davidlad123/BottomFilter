package com.zphinx.filterdialog

import android.content.ContentValues.TAG
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import com.zphinx.filterdialog.adapter.FilterAdapter
import com.zphinx.filterdialog.utils.Util
import kotlinx.android.parcel.Parcelize




/**
 *
 * @author David Ladapo
 *
 *
 */
class FilterDialogManager(val activity: AppCompatActivity,var groupDataFile:String?) {
    private var filterList: ArrayList<FilterGroup>? = null
    private lateinit var filterDialogFragment: FilterDialogFragment

    fun loadFilterGroups(
        onFilterApplied: (ArrayList<FilterGroup>) -> Unit,
        init: ArrayList<FilterGroup>? = null,
        onEmptyFilter: (List<FilterGroup>) -> Unit
    ) {

        filterList = init ?: getGroupData()
        Log.d(TAG,"The filterList obtained was: $filterList")
        filterDialogFragment = FilterDialogFragment.newInstance(
            activity.getString(R.string.filter_current),
            activity.getString(R.string.filter),
            activity.getString(R.string.cancel),
            true,
            true,
            { runFilteredGroups(filterList!!, onFilterApplied, onEmptyFilter) },
            { dismissFilter(filterList!!, onEmptyFilter) },
            {
                clearFilter(filterList!!)
                onFilterApplied(filterList!!)

            },
            FilterAdapter(activity, filterList!!, onFilterApplied)
        )
        filterDialogFragment.show(activity.supportFragmentManager, "Filter_Dialog")

    }

    private fun runFilteredGroups(
        filterGroups: ArrayList<FilterGroup>,
        onFilterApplied: (ArrayList<FilterGroup>) -> Unit,
        onEmptyFilter: (List<FilterGroup>) -> Unit
    ) {
        if (isFilterListLoaded(filterGroups)) {
            onFilterApplied(filterGroups)
        } else {
            onEmptyFilter(filterGroups)
        }
    }

    private fun dismissFilter(filterGroups: ArrayList<FilterGroup>, onEmptyFilter: (List<FilterGroup>) -> Unit) {
        if (!isFilterListLoaded(filterGroups)) {
            onEmptyFilter.invoke(filterGroups)
        }
    }

    private fun clearFilter(filterGroups: ArrayList<FilterGroup>) {
        for (i in 0 until filterGroups.size) {
            val group = filterGroups[i]
            val children = group.groupChildren
            group.isExpanded = false
            for (j in 0 until children.size) {
                val child = children[j]
                child.isChecked = false
            }
        }
        filterDialogFragment.clearChecked()
        filterDialogFragment.dismissAllowingStateLoss()



    }

    private fun isFilterListLoaded(filterGroups: List<FilterGroup>): Boolean {
        val isLoaded = false
        for (i in 0 until filterGroups.size) {
            val children = filterGroups[i].groupChildren
            for (j in 0 until children.size) {
                if (children[j].isChecked) {
                    return true
                }
            }
        }
        return isLoaded
    }

    private fun getGroupData(): ArrayList<FilterGroup> {

        Log.d(TAG,"Running get group data..................")

        lateinit var filterGroups: ArrayList<FilterGroup>
        val dataFile= groupDataFile?:"filter-groups.json"
        filterGroups = try {
            val groupType = object : TypeToken<ArrayList<FilterGroup>>() {}.type
            val someGroups = Util.loadAssetFile(dataFile, activity)
            val gson = GsonBuilder()

                .serializeNulls()
                .create()

            gson.fromJson<ArrayList<FilterGroup>>(someGroups, groupType)
        } catch (e: Exception) {
            Log.d(TAG,"error fetching variants: ${e.message} with exception type:")
            e.printStackTrace()
            ArrayList()
        }
        return filterGroups
    }
}

@Parcelize
data class FilterGroup(
    val id: Long,
    val groupName: String,
    var fieldName: String? = null,
    var groupChildren: Array<GroupChild>,
    var isExpanded: Boolean = false
) : Parcelable {

    init {
        fieldName ?: groupName
    }

    override fun toString(): String {
        return "FilterGroup(id=$id, groupName='$groupName', fieldName=$fieldName, groupChildren=${groupChildren.contentToString()}, isExpanded=$isExpanded)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilterGroup

        if (id != other.id) return false
        if (groupName != other.groupName) return false
        if (fieldName != other.fieldName) return false
        if (!groupChildren.contentEquals(other.groupChildren)) return false
        if (isExpanded != other.isExpanded) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + groupName.hashCode()
        result = 31 * result + (fieldName?.hashCode() ?: 0)
        result = 31 * result + groupChildren.contentHashCode()
        result = 31 * result + isExpanded.hashCode()
        return result
    }


}

@Parcelize
data class GroupChild(val id: Long, val name: String, var fieldName: String? = null, var isChecked: Boolean = false) : Parcelable {


    init {
        fieldName ?: name
    }


    override fun toString(): String {
        return "GroupChild(id=$id, name='$name', fieldName=$fieldName, isChecked=$isChecked)"
    }


}