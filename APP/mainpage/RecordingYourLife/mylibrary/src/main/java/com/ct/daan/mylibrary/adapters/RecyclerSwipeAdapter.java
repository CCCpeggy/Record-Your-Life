package com.ct.daan.mylibrary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ct.daan.mylibrary.SwipeLayout;
import com.ct.daan.mylibrary.implments.SwipeItemMangerImpl;
import com.ct.daan.mylibrary.interfaces.SwipeAdapterInterface;
import com.ct.daan.mylibrary.interfaces.SwipeItemMangerInterface;
import com.ct.daan.mylibrary.util.Attributes;

import java.util.List;

public abstract class RecyclerSwipeAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH viewHolder, final int position);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
