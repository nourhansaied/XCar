package com.victoria.driver.util.advance_adapter.base;

/**
 * Created by hlink21 on 21/7/16.
 */
public interface HasItem<E> {
    E getItem(int index);

    E getSearchItem(int index);
}
