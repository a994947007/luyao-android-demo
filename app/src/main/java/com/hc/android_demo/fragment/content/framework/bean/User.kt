package com.hc.android_demo.fragment.content.framework.bean

import com.hc.base.framework.Syncable

data class User(
    var id: String,
    var username: String,
    var name: String,
    var isLogin: Boolean = false
): Syncable {

    override fun key(): String {
        return id
    }

}
