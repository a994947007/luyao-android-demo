package com.hc.recyclerView;

public class MoreTypeBean {
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    public int type;
    public int icon;
    public String title;

    public MoreTypeBean(int type, int icon, String title) {
        this.type = type;
        this.icon = icon;
        this.title = title;
    }
}
