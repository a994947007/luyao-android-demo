package com.hc.android_demo.fragment.content.framework.bean

import com.hc.base.framework.Syncable

data class User(
    var id: String,
    var username: String,
    var name: String,
    var isLogin: Boolean = false
) {
}

class UserSyncable(val user: User): Syncable<User>{
    override fun key(): String {
        return user.id
    }

    override fun onSync(target: User) {
        user.username = target.username
        user.isLogin = target.isLogin
        user.name = target.name
    }

}
