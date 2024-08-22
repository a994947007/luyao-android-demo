package com.hc.android_demo.fragment.content.framework.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hc.android_demo.R
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants

@ARouter(path = FragmentConstants.LIGHT_DARK_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class LightDarkTestFragment : LuFragment() {

    private var isDark = true

    fun setDark(isDark: Boolean) {
        this.isDark = isDark
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflater2 = LayoutInflater.from(context?.createContext())
        return inflater2.inflate(R.layout.fragment_light_dark_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun Context.createContext(): Context {
        val configuration = Configuration()
        configuration.setTo(
            context?.resources?.configuration
        )
        if (isDark) {
            configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
        } else {
            configuration.uiMode = Configuration.UI_MODE_NIGHT_NO
        }
        return createConfigurationContext(configuration)
    }
}