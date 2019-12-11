package edu.mylanecckanec.overlapper;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Map;

public class UrlAdapter extends ArrayAdapter {

    private Context mContext;
    private Map<String,String> urls;

    public UrlAdapter(Context mContext, int resource, Map<String,String> urls) {
        super(mContext, resource);
        this.mContext = mContext;
        this.urls = urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public void addUrl(String name, String url) {
        this.urls.put(name,url);
    }
}
