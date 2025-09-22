package com.hc.android_demo.fragment.content.dynamic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.hc.android_demo.R;
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants

@ARouter(
    path = FragmentConstants.JETPACK_COMPOSE_VIEW_ID,
    group = FragmentConstants.APP_DYNAMIC
)
class ComposeViewFragment: LuFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compose_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            helloWordText()
        }
    }
}


@Composable
fun helloWordText() {

    var isClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Hello World!",
            modifier = Modifier
                .clickable {
                    isClicked = !isClicked
                    Toast.makeText(context, "Hello Toast!", Toast.LENGTH_SHORT).show()
                },
            color = if (isClicked) Color.Red else Color.Blue,
            fontSize = 24.sp
        )
    }
}