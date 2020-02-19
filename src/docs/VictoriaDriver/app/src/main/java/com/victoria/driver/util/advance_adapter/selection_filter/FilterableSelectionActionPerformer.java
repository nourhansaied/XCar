package com.victoria.driver.util.advance_adapter.selection_filter;

/**
 * Created by hlink on 23/1/18.
 */

public interface FilterableSelectionActionPerformer<E extends FilterableAdapter.Selectable> {

    E getItem(int index);

    void select(int index);

    void select(E item);

    void deSelect(int index);

    void deSelect(E item);

    @FilterableAdapter.SelectionMode
    int getSelectionMode();
}
