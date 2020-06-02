package com.zphinx.bottomfilter.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zphinx.bottomfilter.helpers.PostageFilterHelper
import com.zphinx.bottomfilter.R
import com.zphinx.bottomfilter.model.Postage
import com.zphinx.filterdialog.FilterDialogManager
import com.zphinx.filterdialog.FilterGroup
import com.zphinx.filterdialog.utils.Util
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*
import kotlinx.android.synthetic.main.postage_list_content.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SampleFilterFragment : Fragment() {
    private var currentFilters: ArrayList<FilterGroup>?=null
    private lateinit var recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.recycler
        initRecycler()
        button_first.setOnClickListener{
            onFilterClicked(it)
        }



    }
    fun onFilterClicked(view: View?) {
        Log.d("SampleFilterFragment","Filter clicked ..calling action to render")
        view?.let {
            FilterDialogManager(requireActivity() as AppCompatActivity,"letter-groups.json").loadFilterGroups(
                onFilterApplied = ::applyFilter,
                init = currentFilters,
                onEmptyFilter = ::onEmptyFilter
            )
        }

    }
    private fun applyFilter(filterList: java.util.ArrayList<FilterGroup>?) {
        onFilterApplied(filterList, null)
    }
    private fun onFilterApplied(filterList: ArrayList<FilterGroup>?, listToFilter: List<Postage>? = null){

        currentFilters = filterList
        //filter is mutually inclusive for inner values and exclusive for Outer values
        currentFilters?.let { searchFilter ->
            processFilter(listToFilter, searchFilter)

        }?: adapter.updateList(listToFilter as ArrayList<Postage> ?: postages)
    }
    private fun processFilter(listToFilter: List<Postage>?, searchFilter: ArrayList<FilterGroup>) {
        val toFilter = listToFilter?:postages
        val newList: List<Postage>?
        when {
            searchFilter.isEmpty() -> {
                newList = toFilter
            }
            else -> {
                //filter_switch.isChecked = true
                newList = toFilter.filter {
                    PostageFilterHelper()
                        .matchFilter(searchFilter, it)
                }
            }
        }
        adapter.updateList(newList as ArrayList<Postage>?)

    }

    private fun onEmptyFilter(filterList: List<FilterGroup>? = null) {
        currentFilters = null

    }
    lateinit var postages: ArrayList<Postage>
    lateinit var  adapter: PostageListAdapter
    private fun initRecycler() {

        try {
            val ffType = object : TypeToken<ArrayList<Postage>>() {}.type
            val someHelp = Util.loadAssetFile("postage.json", requireContext())
            val gson = GsonBuilder()
                 .serializeNulls()
                .create()
            postages = gson.fromJson(someHelp, ffType)
            Log.d("SampleFilterFragment", "postages are: $postages")
            adapter = PostageListAdapter(postages)
            recycler.adapter = adapter

        } catch (e: Exception) {
            Log.e(TAG, "error fetching friends and family: ${e.message}")
        }


    }

    inner class PostageListAdapter(var postages: ArrayList<Postage>?) :
        RecyclerView.Adapter<PostageListAdapter.PostageListHolder>() {

        fun updateList(list: ArrayList<Postage>?){
            postages = list
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostageListHolder {

            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.postage_list_content, parent, false)
            return PostageListHolder(view)
        }

        inner class PostageListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val statusView: TextView = itemView.status_value as TextView
            val statusIdView: TextView = itemView.status_id_value as TextView
            val locationView: TextView = itemView.location_value as TextView
            val typeView: TextView = itemView.type_value as TextView
            val descriptionView: TextView = itemView.description_value as TextView
        }

        override fun getItemCount(): Int {
            return postages?.size ?: 0
        }

        override fun onBindViewHolder(holder: PostageListHolder, position: Int) {
            postages?.let {
                val postage = it[position]
                holder.statusView.text = postage.status
                holder.statusIdView.text = postage.statusId.toString()
                holder.locationView.text = postage.location
                holder.typeView.text = postage.type
                holder.descriptionView.text = postage.description
            }
        }
    }

}




