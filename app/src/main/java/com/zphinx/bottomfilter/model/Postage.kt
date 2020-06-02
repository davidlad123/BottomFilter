package com.zphinx.bottomfilter.model

import com.zphinx.bottomfilter.helpers.PostageFilterable

data class Postage(
    val id: Int,
    var statusId: Int,
    override var status: String? = null,
    override var type: String? = null,
    val description: String,
    var location: String
) : PostageFilterable {

}
