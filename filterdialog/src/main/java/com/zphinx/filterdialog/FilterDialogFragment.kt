package com.zphinx.filterdialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zphinx.filterdialog.adapter.FilterAdapter
import kotlinx.android.synthetic.main.filter_dialog.*

import kotlinx.android.synthetic.main.common_dialog.dialogTitle
import kotlinx.android.synthetic.main.filter_dialog.view.*


/**
 *
 * @author David Ladapo
  */

class FilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var title: String
    private lateinit var okButton: String
    private lateinit var cancelButton: String
    private lateinit var clearButton: String

    private lateinit var adapter: FilterAdapter
    private var canCancel: Boolean = false
    private var isClearButton: Boolean = false
    var okFunction: () -> Unit = {}
    var cancelFunction: () -> Unit = {}
    var clearFunction: () -> Unit = {}

    private lateinit var listView: ExpandableListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments!!.getString("title", "")
        okButton = arguments!!.getString("okButton", "")
        cancelButton = arguments!!.getString("cancelButton", "")
        isClearButton = arguments!!.getBoolean("isClearButton")
        canCancel = arguments!!.getBoolean("canCancel")

        setStyle(STYLE_NO_TITLE, 0)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = R.layout.filter_dialog
        val view = inflater.inflate(layout, container)
        listView = view.filter_list as ExpandableListView
        listView.setAdapter(adapter)
        listView.setOnGroupExpandListener {

            val size = adapter.groupCount
            for (i in 0 until size) {
                if (i != it && adapter.filterList[i].isExpanded) {
                    listView.collapseGroup(i)
                    adapter.filterList[i].isExpanded = false

                } else if (i == it) {
                    adapter.filterList[i].isExpanded = true
                    val children = adapter.filterList[i].groupChildren
                    for (j in 0 until children.size) {
                        adapter.updateChildView(it, j, children[j].isChecked)
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogTitle.text = title

        isCancelable = canCancel
        negative_button.text = cancelButton
        positive_button.text = okButton

        if (!isClearButton) {
            negative_button.visibility = View.INVISIBLE
        }

        setButtonListeners()
    }

    private fun setButtonListeners() {

        negative_button.setOnClickListener {
            cancelFunction.invoke()
            dismissAllowingStateLoss()
        }
        positive_button.setOnClickListener {
            okFunction.invoke()
            dismissAllowingStateLoss()
        }
        clear_button.setOnClickListener {
            clearFunction.invoke()

        }
    }

    override fun show(manager: androidx.fragment.app.FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("FilterDialogFragment", "Exception: ${e.message}")
        }
    }

    fun clearChecked() {
        adapter.notifyDataSetChanged()
    }

    
    companion object {
    
        fun newInstance(
            title: String,
            okButton: String,
            cancelButton: String,
            isClearButton: Boolean,
            canCancel: Boolean,
            positiveFunction: () -> Unit,
            negativeFunction: () -> Unit,
            clearFunction: () -> Unit,
            someAdapter: FilterAdapter
        ): FilterDialogFragment {
            val f = FilterDialogFragment()

            var args = Bundle()
            args.putString("title", title)
            args.putString("okButton", okButton)
            args.putString("cancelButton", cancelButton)
            args.putBoolean("isClearButton", isClearButton)
            args.putBoolean("canCancel", canCancel)
            f.arguments = args
            f.okFunction = positiveFunction
            f.cancelFunction = negativeFunction
            f.clearFunction = clearFunction
            f.adapter = someAdapter
            return f
        }


    }
}
