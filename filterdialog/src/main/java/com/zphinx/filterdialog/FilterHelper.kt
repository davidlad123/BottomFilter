package com.zphinx.filterdialog


/**
 * A helper class for dealing with the variants on this app.
 *  Extend this class and override the method findFieldValue(fieldName: String?, filterable: Filterable): String.
 *
 *  The default implementation shown here is an example of how its done
 *
 * Filter helper contains a default implementation which should be overriden to accomodate your specific filter terms and names.
 */
open class FilterHelper {

    /**
     * Match a variant based on the relationship diagram depicted by
     * A(e||f||g)  &&  B(a||b||c)  &&  C(d||e||f)
     *
     * @param searchFilter List<FilterGroup>
     * @param filterable A Filterable instance
     * @return Boolean
     */
    fun matchFilter(searchFilter: List<FilterGroup>, filterable: Filterable): Boolean {

        var totalMatch = true
        var resultList = mutableListOf<Int>()
        for (i in 0 until searchFilter.size) {
            val children = searchFilter[i].groupChildren
            val groupName = searchFilter[i].groupName

            val hasMatch = findMatchingChildren(children, groupName, filterable)
            resultList.add(hasMatch)

        }
        val subList = resultList.filter {
            it > 0
        }
        for (i in 0 until subList.size) {
            if (i == 0) {
                totalMatch = subList[0] == 1
            } else {
                totalMatch = totalMatch && subList[i] == 1
            }
        }
        return totalMatch
    }

    /**
     * Return 0,1 or 2
     * 0 = no children activated
     * 1=matching child found
     * 2=no matching child found
     *
     * @param children Array<GroupChild>
     * @param groupName String?
     * @param filterable A Filterable instance
     * @return Int
     */
    private fun findMatchingChildren(children: Array<GroupChild>, groupName: String?, filterable: Filterable): Int {
        var isMatch = false
        val resultList = mutableListOf<Int>()
        for (j in 0 until children.size) {
            val child = children[j]
            if (child.isChecked) {
                isMatch = findFieldValue(groupName, filterable) == child.fieldName ?: child.name
                resultList.add(if (isMatch) 1 else 2)
            } else {
                resultList.add(0)
            }

        }
        val subList = resultList.filter {
            it > 0
        }
        return when {
            subList.isEmpty() -> {
                0
            }
            subList.contains(1) -> {
                1
            }
            else -> 2
        }
    }

    open fun findFieldValue(fieldName: String?, filterable: Filterable): String {

        return when (fieldName) {
            "type" -> (filterable as LocalFilterable).type!!
            "name" -> (filterable as LocalFilterable).name!!
            "status" -> (filterable as LocalFilterable).status!!
            else -> {
                "NA"
            }
        }
    }

    class LocalFilterable : Filterable {
        var type: String? = null
        var name: String? = null
        var status: String? = null

    }
}
