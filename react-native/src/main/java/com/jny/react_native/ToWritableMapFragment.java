package com.jny.react_native;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.base.fragment.LuFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;

@ARouter(path = FragmentConstants.REACT_NATIVE_TO_WRITABLE_MAP,
        group = FragmentConstants.DYNAMIC)
public class ToWritableMapFragment extends LuFragment {

    String code = "" +
            "        TestBean testBean = new TestBean();\n" +
            "        testBean.feedId = 123;\n" +
            "        testBean.name = \"abc\";\n" +
            "        testBean.urls = new ArrayList<>();\n" +
            "        testBean.urls.add(\"url1\");\n" +
            "        testBean.urls.add(\"url2\");\n" +
            "        testBean.otherBeans = new ArrayList<>();\n" +
            "        OtherBean otherBean = new OtherBean();\n" +
            "        otherBean.name = \"123\";\n" +
            "        OtherBean otherBean2 = new OtherBean();\n" +
            "        otherBean.name = \"321\";\n" +
            "        testBean.otherBeans.add(otherBean);\n" +
            "        testBean.otherBeans.add(otherBean2);\n" +
            "        TextView tv = view.findViewById(R.id.text);";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.react_to_writable_map_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TestBean testBean = new TestBean();
        testBean.feedId = 123;
        testBean.name = "abc";
        testBean.urls = new ArrayList<>();
        testBean.urls.add("url1");
        testBean.urls.add("url2");
        testBean.otherBeans = new ArrayList<>();
        OtherBean otherBean = new OtherBean();
        otherBean.name = "123";
        OtherBean otherBean2 = new OtherBean();
        otherBean.name = "321";
        testBean.otherBeans.add(otherBean);
        testBean.otherBeans.add(otherBean2);
        TextView tv = view.findViewById(R.id.text);
        try {
            tv.setText(code + "\n" + RNUtils.toWritableMap(testBean).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
