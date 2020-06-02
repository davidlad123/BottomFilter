package com.zphinx.bottomfilter.helpers

import com.zphinx.bottomfilter.helpers.PostageFilterable
import com.zphinx.filterdialog.FilterHelper
import com.zphinx.filterdialog.Filterable


/**
 *
 * @author David Ladapo
 *
 * Created  on 02 Jun 2020 18:48
 * @copyright Zphinx Software Solutions 2020
 *
 */
class PostageFilterHelper: FilterHelper() {

    override fun findFieldValue(fieldName: String?, filterable: Filterable): String {

        return when (fieldName) {
            "Type"  -> (filterable as PostageFilterable).type!!
            "Status" -> (filterable as PostageFilterable).status!!
            else -> {
                "NA"
            }
        }
    }
}