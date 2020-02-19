package com.victoria.customer.util.advance_adapter.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.victoria.customer.R;
import com.victoria.customer.util.advance_adapter.base.BaseHolder;
import com.victoria.customer.util.advance_adapter.base.HasItem;
import com.victoria.customer.util.advance_adapter.base.LoadingHolder;
import com.victoria.customer.util.advance_adapter.base.NoDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlink21 on 20/7/16.
 */
public abstract class AdvanceRecycleViewAdapter<H extends BaseHolder, E>
        extends RecyclerView.Adapter<BaseHolder> implements HasItem<E> {

    protected List<E> items;

    public boolean isNoData = false;
    public boolean isLoading = true;
    protected String errorMessage = "";
    private static int MAX_ITEM_PER_PAGE = 20;
    protected List<E> searchItems;
    public boolean isSearchable = false;
    private String message = "";

    public AdvanceRecycleViewAdapter() {
    }

    public AdvanceRecycleViewAdapter(List<E> items) {
        this.items = items;
    }

    public void setItems(List<E> items) {
        if (this.items == null)
            this.items = new ArrayList<>();
        else this.items.clear();

        this.items.addAll(items);
        isSearchable = isLoading = false;
        notifyDataSetChanged();
    }

    public void setItems(List<E> items, int page) {
        isLoading = true;
        if (page == 1) {
            if (this.items == null)
                this.items = new ArrayList<>();
            else this.items.clear();
            this.items.addAll(items);

            if (this.items.size() < MAX_ITEM_PER_PAGE)
                isLoading = false;

            notifyDataSetChanged();
        } else {
            if (this.items != null) {
                this.items.addAll(items);
                notifyDataSetChanged();
            }
        }
    }


    public void setSearchItems(List<E> searchItems) {
        if (this.searchItems == null)
            this.searchItems = new ArrayList<>();
        else this.searchItems.clear();

        this.searchItems.addAll(searchItems);
        isLoading = false;
        isSearchable = true;
        notifyDataSetChanged();
    }

    public <T> void loadCircleImage(T path, AppCompatImageView appCompatImageView) {
        Glide.with(appCompatImageView.getContext())
                .load(path)
                .apply(RequestOptions.circleCropTransform())
                .into(appCompatImageView);
    }

    public <T> void loadImage(T path, AppCompatImageView appCompatImageView) {
        Glide.with(appCompatImageView.getContext())
                .load(path)
                .into(appCompatImageView);
    }

    public void removeItem(E e) {
        int i = items.indexOf(e);
        items.remove(i);
        notifyItemRemoved(i);
    }

    public void removeAddAtFirst(E e) {
        int i = items.indexOf(e);
        E remove = items.remove(i);
        notifyItemRemoved(i);
        addItem(remove, 0);
    }

    public void addItem(E e) {
        if (items == null) {
            items = new ArrayList<>();
            isLoading = false;
        }

        items.add(e);
        notifyItemInserted(items.size());
    }

    public void addItem(E e, int position) {

        if (items == null) {
            items = new ArrayList<>();
            isLoading = false;
            items.add(e);
            notifyDataSetChanged();
        } else {
            items.add(position, e);
            notifyItemInserted(position);
        }

    }

    public void updateItem(E e) {
        if (items != null) {
            int index = items.indexOf(e);
            if (index > -1) {
                items.set(index, e);
                notifyDataSetChanged();
            }
        }
    }

    public void clearAllItem() {
        if (items != null) {
            items.clear();
            isLoading = true;
            notifyDataSetChanged();
        }
    }

    public List<E> getItems() {
        return items;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        isLoading = false;
        isNoData = true;
        notifyDataSetChanged();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BaseHolder baseHolder;

        if (viewType == 333)
            baseHolder = createLoadingHolder(parent, viewType);
        else if (viewType == 111)
            baseHolder = createNoDataHolder(parent, viewType);
        else
            baseHolder = createDataHolder(parent, viewType);

        baseHolder.setHasItem(this);

        return baseHolder;
    }

    AppCompatTextView textViewMessage;

    public NoDataHolder createNoDataHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_no_data, parent, false);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        if (!message.equalsIgnoreCase("")) {
            textViewMessage.setText(message);
        }
        return new NoDataHolder(view);
    }

    public LoadingHolder createLoadingHolder(ViewGroup parent, int viewType) {
        ProgressBar progressBar = new ProgressBar(parent.getContext(), null, android.R.attr.progressBarStyleSmall);

        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        progressBar.setLayoutParams(layoutParams);

        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).gravity = Gravity.CENTER;
        }

        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading, parent, false);
        return new LoadingHolder(progressBar);
    }

    public abstract H createDataHolder(ViewGroup parent, int viewType);

    @Override
    public final int getItemViewType(int position) {

        if (isNoData && isLoading)
            return 333;
        else if (!isLoading && isNoData)
            return 111;
        else if (isLoading && items.size() == position)
            return 333;

        return getViewType(position);
    }

    public int getViewType(int position) {
        return 222;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType != 111 && itemViewType != 333) {
            E item = isSearchable ? getSearchItem(position) : getItem(position);
            // actual view
            onBindDataHolder((H) holder, position, item);
        }

        if (itemViewType == 111 && holder instanceof NoDataHolder) {
            ((NoDataHolder) holder).setErrorText(errorMessage);
        }
    }


    @Override
    public E getSearchItem(int position) {
        if (isLoading && searchItems.size() == position) {
            int i = position - 1;
            i = i < 0 ? 0 : i;
            return searchItems.get(i);
        }
        return searchItems.get(position);
    }


    public abstract void onBindDataHolder(H holder, int position, E item);


    @Override
    public final E getItem(int position) {

        if (isLoading && items.size() == position) {
            int i = position - 1;
            i = i < 0 ? 0 : i;
            return items.get(i);
        }

        return items.get(position);
    }

    @Override
    public final int getItemCount() {
        isNoData = items == null || items.isEmpty();

        if (isNoData)
            return 1;
        else if (isLoading) {
            return isSearchable ? searchItems.size() + 1 : items.size() + 1;
        } else return isSearchable ? searchItems.size() : items.size();
    }


    protected View makeView(ViewGroup parent, @LayoutRes int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    public void setMessageToSet(String s) {
        message = s;
    }
}