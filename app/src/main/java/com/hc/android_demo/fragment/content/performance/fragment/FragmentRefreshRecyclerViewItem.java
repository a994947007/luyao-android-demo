package com.hc.android_demo.fragment.content.performance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.base.fragment.LuFragment;
import com.hc.recycler.RecyclerUserHelper;
import com.hc.recycler.UserModel;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

@ARouter(path = FragmentConstants.PERFORMANCE_REFRESH_RECYCLER_VIEW_ITEM,
        group = FragmentConstants.PERFORMANCE)
public class FragmentRefreshRecyclerViewItem extends LuFragment {

    private RecyclerView recyclerView;
    private View refreshBtn;

    private final List<UserModel> list = new ArrayList<>();
    {
        list.add(new UserModel("001", "abc", "https://img2.baidu.com/it/u=175449109,3788073609&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1690218000&t=71188f96dcd548a4e04c1e1c0fe54928", "你好"));
        list.add(new UserModel("002", "def", "https://img0.baidu.com/it/u=1741358583,937962297&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1690218000&t=6742dad5b6e79abcdf73f1669b138e86", "你好"));
        list.add(new UserModel("003", "ghi", "https://img0.baidu.com/it/u=2222208386,2750243488&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1690218000&t=45e2dd2031bc020a8c94321770253725", "你好"));
        list.add(new UserModel("004", "jkl", "https://img0.baidu.com/it/u=3825582109,2484598980&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1690218000&t=67a7f165947150bf53779eadf33b3e3b", "你好"));
        list.add(new UserModel("005", "abd", "https://img1.baidu.com/it/u=2386067069,873630896&fm=253&fmt=auto&app=138&f=JPEG?w=530&h=500", "你好"));
        list.add(new UserModel("006", "eqw", "https://img0.baidu.com/it/u=2060863300,345582671&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("007", "zcv", "https://img0.baidu.com/it/u=484134802,728302546&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("008", "fad", "https://img1.baidu.com/it/u=2275831821,883710495&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("009", "trw", "https://img0.baidu.com/it/u=943255980,3727070805&fm=253&fmt=auto&app=138&f=JPEG?w=501&h=500", "你好"));
        list.add(new UserModel("010", "ghd", "https://img1.baidu.com/it/u=3661057457,567145783&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("011", "jhf", "https://img1.baidu.com/it/u=4271466778,3653777022&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("012", "vbb", "https://img0.baidu.com/it/u=585970814,1998273140&fm=253&fmt=auto&app=138&f=JPEG?w=475&h=475", "你好"));
        list.add(new UserModel("013", "daa", "https://img0.baidu.com/it/u=3196160345,1440645038&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
        list.add(new UserModel("014", "ddd", "https://img0.baidu.com/it/u=1600544911,3181397926&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=501", "你好"));
        list.add(new UserModel("015", "fff", "https://img0.baidu.com/it/u=1253127582,3275255899&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "你好"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recycler_view_item_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerUserHelper.updateAndDiffItem(recyclerView, list);
        refreshBtn = view.findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(v -> {
            List<UserModel> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i == 3) {
                    UserModel userModel = new UserModel(list.get(i));
                    userModel.avatar = "https://img2.baidu.com/it/u=2400906162,1815463471&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=554";
                    newList.add(userModel);
                } else if (i == 4) {
                    UserModel userModel = new UserModel(list.get(i));
                    userModel.username = "erqrwerqwq";
                    newList.add(userModel);
                } else if (i == 10) {
                    UserModel userModel = new UserModel(list.get(i));
                    userModel.msg = "哈哈哈哈哈哈哈哈哈哈";
                    newList.add(userModel);
                } else if (i == 11) {
                    UserModel userModel = new UserModel(list.get(i));
                    userModel.msg = "牛皮";
                    newList.add(userModel);
                } else {
                    newList.add(new UserModel(list.get(i)));
                }
            }
            RecyclerUserHelper.updateAndDiffItem(recyclerView, newList);
        });
    }
}
