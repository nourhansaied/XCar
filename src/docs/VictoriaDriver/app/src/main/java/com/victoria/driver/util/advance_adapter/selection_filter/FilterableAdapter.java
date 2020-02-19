package com.victoria.driver.util.advance_adapter.selection_filter;

import android.support.annotation.IntDef;
import android.widget.Filter;
import android.widget.Filterable;

import com.victoria.driver.util.advance_adapter.OnSelectionChangeListener;
import com.victoria.driver.util.advance_adapter.adapter.AdvanceRecycleViewAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlink on 23/1/18.
 */

public abstract class FilterableAdapter<H extends FilterSelectableViewHolder, E extends FilterableAdapter.Selectable>
        extends AdvanceRecycleViewAdapter<H, E> implements FilterableSelectionActionPerformer<E>, Filterable {


    public static final int NONE = -1;
    public static final int SINGLE = 0;
    public static final int MULTI = 1;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setSearchItems((List<E>) results.values);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<E> FilteredArrayNames = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < getItems().size(); i++) {
                    E dataNames = getItems().get(i);
                    if (dataNames.toString().toLowerCase().contains(constraint.toString())) {
                        FilteredArrayNames.add(dataNames);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;

                return results;
            }
        };
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, SINGLE, MULTI})
    public @interface SelectionMode {
    }

    private E previousSelectedItem = null;
    private int previousSelectedItemIndex = -1;
    @SelectionMode
    private int selectionModes = SINGLE;
    private int maxSelection;

    private OnSelectionChangeListener<E> onSelectionChangeListener;

    public FilterableAdapter(@SelectionMode int selectionModes) {
        this.selectionModes = selectionModes;
    }

    public FilterableAdapter(@SelectionMode int selectionModes, int maxSelection) {
        this.selectionModes = selectionModes;
        this.maxSelection = maxSelection;
    }

    public FilterableAdapter(List<E> items, @SelectionMode int selectionModes) {
        super(items);
        this.selectionModes = selectionModes;
    }

    public FilterableAdapter(List<E> items, @SelectionMode int selectionModes, int maxSelection) {
        super(items);
        this.selectionModes = selectionModes;
        this.maxSelection = maxSelection;
    }

    public void addSelectionChangeListener(OnSelectionChangeListener<E> onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }

    @SelectionMode
    public int getSelectionModes() {
        return selectionModes;
    }

    public List<E> getSelectedItems() {
        List<E> selectedElements = new ArrayList<>();
        if (items != null) {
            /*Observable.fromIterable(items).map((Function<E, E>) selectedElements1 -> {
                if(selectedElements1.isSelected())
                    return selectedElements1;

            }).toList().doOnSuccess(selectedElements::addAll).subscribe();*/
            for (E e : items) {
                if (e.isSelected())
                    selectedElements.add(e);
            }
        }
        return selectedElements;
    }

    @Override
    public void select(int index) {
        if (index < 0)
            index = 0;

        E item = isSearchable ? getSearchItem(index) : getItem(index);

        if (selectionModes == SINGLE) {
            if (previousSelectedItem != null) {
                previousSelectedItem.setSelected(false);
                notifyItemChanged(previousSelectedItemIndex);
            }
        }

        //CURRENT ITEM
        item.setSelected(true);

        if (onSelectionChangeListener != null)
            onSelectionChangeListener.onSelectionChange(item, true);

        //notifyItemChanged(index);
        notifyDataSetChanged();

        previousSelectedItem = item;
        previousSelectedItemIndex = index;
    }

    public void select(E e) {
        int i = items.indexOf(e);
        select(i);
    }

    @Override
    public void deSelect(int index) {
        E item = isSearchable ? getSearchItem(index) : getItem(index);
        item.setSelected(false);

        if (onSelectionChangeListener != null)
            onSelectionChangeListener.onSelectionChange(item, false);

        //notifyItemChanged(index);
        notifyDataSetChanged();
    }

    public void deSelect(E item) {
        deSelect(items.indexOf(item));
    }

    @Override
    public int getSelectionMode() {
        return selectionModes;
    }


    public E getPreviousSelectedItem() {
        return previousSelectedItem;
    }

    public int getPreviousSelectedItemIndex() {
        return previousSelectedItemIndex;
    }

    @Override
    public void onBindDataHolder(H holder, int position, E item) {
        onBindSelectedViewHolder(holder, position, item);
        if (previousSelectedItem == null && item.isSelected()) {
            previousSelectedItem = item;
            previousSelectedItemIndex = position;
        }
    }

    public abstract void onBindSelectedViewHolder(H h, int position, E item);

    public interface Selectable {
        void setSelected(boolean selected);

        boolean isSelected();
    }
}