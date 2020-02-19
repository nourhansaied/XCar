package com.victoria.customer.util.advance_adapter.selection_filter;

import android.view.View;

import com.victoria.customer.util.advance_adapter.OnRecycleItemClick;
import com.victoria.customer.util.advance_adapter.base.BaseHolder;


/**
 * Created by hlink on 23/1/18.
 */

public class FilterSelectableViewHolder<E extends FilterableAdapter.Selectable> extends BaseHolder<E> {

    FilterableSelectionActionPerformer<E> actionPerformer;


    public FilterSelectableViewHolder(View itemView, FilterableSelectionActionPerformer<E> actionPerformer) {
        super(itemView);
        this.actionPerformer = actionPerformer;
    }

    public FilterSelectableViewHolder(View itemView, FilterableSelectionActionPerformer<E> actionPerformer, OnRecycleItemClick<E> onRecycleItemClick) {
        super(itemView, onRecycleItemClick);
        this.actionPerformer = actionPerformer;
    }

    public E getCurrentItem() {
        return actionPerformer.getItem(getLayoutPosition());
    }

    public void select() {
        actionPerformer.select(getAdapterPosition());
    }

    public void select(E item){
        actionPerformer.select(item);
    }

    public void deSelect() {
        actionPerformer.deSelect(getAdapterPosition());
    }

    public void deSelect(E item){
        actionPerformer.deSelect(item);
    }

    @FilterableAdapter.SelectionMode
    public int getSelectionMode() {
        return actionPerformer.getSelectionMode();
    }
}
