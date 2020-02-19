package com.victoria.customer.util.advance_adapter.selection;

import android.view.View;

import com.victoria.customer.util.advance_adapter.OnRecycleItemClick;
import com.victoria.customer.util.advance_adapter.base.BaseHolder;

public class SelectableViewHolder<E extends AbstractSelectableAdapter.Selectable> extends BaseHolder<E> {

    SelectionActionPerformer<E> actionPerformer;


    public SelectableViewHolder(View itemView, SelectionActionPerformer<E> actionPerformer) {
        super(itemView);
        this.actionPerformer = actionPerformer;
    }

    public SelectableViewHolder(View itemView, SelectionActionPerformer<E> actionPerformer, OnRecycleItemClick<E> onRecycleItemClick) {
        super(itemView, onRecycleItemClick);
        this.actionPerformer = actionPerformer;
    }

    public E getCurrentItem() {
        return actionPerformer.getItem(getLayoutPosition());
    }

    public void select() {
        actionPerformer.select(getAdapterPosition());
    }

    public void deSelect() {
        actionPerformer.deSelect(getAdapterPosition());
    }

    @AbstractSelectableAdapter.SelectionMode
    public int getSelectionMode() {
        return actionPerformer.getSelectionMode();
    }
}