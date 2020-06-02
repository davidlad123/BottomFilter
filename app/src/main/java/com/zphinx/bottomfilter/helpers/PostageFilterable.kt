package com.zphinx.bottomfilter.helpers

import com.zphinx.filterdialog.Filterable

interface PostageFilterable : Filterable {
    var status: String?
    var type: String?

}
