package com.ct.daan.recordingyourlife.adapter;

import android.content.Context;

import com.ct.daan.mylibrary.adapters.ArraySwipeAdapter;
import com.ct.daan.recordingyourlife.R;

import java.util.List;

/**
 * Sample usage of ArraySwipeAdapter.
 * @param <T>
 */
public class ArraySwipeAdapterSample<T> extends ArraySwipeAdapter {
    public ArraySwipeAdapterSample(Context context, int resource) {
        super(context, resource);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ArraySwipeAdapterSample(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
