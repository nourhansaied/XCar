package com.victoria.customer.util.advance_adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victoria.customer.util.advance_adapter.DebouncedOnClickListener;
import com.victoria.customer.util.advance_adapter.OnRecycleItemClick;


public class BaseHolder<E> extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnRecycleItemClick<E> onRecycleItemClick;
    HasItem<E> hasItem;
    AdvanceClickListener advanceClickListener;

    public BaseHolder(View itemView) {
        super(itemView);
        advanceClickListener = new AdvanceClickListener(1000);
        itemView.setOnClickListener(this);
    }

    public BaseHolder(View itemView, OnRecycleItemClick<E> onRecycleItemClick) {
        super(itemView);
        advanceClickListener = new AdvanceClickListener(1000);
        this.onRecycleItemClick = onRecycleItemClick;
        itemView.setOnClickListener(this);
    }

    public void setHasItem(HasItem<E> hasItem) {
        this.hasItem = hasItem;
    }

    @Override
    public void onClick(View v) {
        if (onRecycleItemClick != null) {
            advanceClickListener.onClick(v);
        }
    }

    public E getCurrent() {
        return hasItem.getItem(getLayoutPosition());
    }

    private class AdvanceClickListener extends DebouncedOnClickListener {
        /**
         * The one and only constructor
         *
         * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
         */
        public AdvanceClickListener(long minimumIntervalMsec) {
            super(minimumIntervalMsec);
        }

        @Override
        public void onDebouncedClick(View v) {
            onRecycleItemClick.onClick(hasItem.getItem(getLayoutPosition()), v);
        }
    }

}