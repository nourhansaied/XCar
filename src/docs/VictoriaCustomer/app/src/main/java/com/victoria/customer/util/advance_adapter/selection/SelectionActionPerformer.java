package com.victoria.customer.util.advance_adapter.selection;

/**
 * Created by hlink21 on 10/6/16.
 */
public interface SelectionActionPerformer<E extends AbstractSelectableAdapter.Selectable> {

    E getItem(int index);

    void select(int index);

    void select(E item);

    void deSelect(int index);

    void deSelect(E item);

    @AbstractSelectableAdapter.SelectionMode
    int getSelectionMode();
}
