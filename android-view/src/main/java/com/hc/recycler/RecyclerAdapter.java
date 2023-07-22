package com.hc.recycler;

import java.util.List;

public interface RecyclerAdapter<T> {
    void setData(List<T> list);

    List<T> getData();
}
