package com.victoria.driver.ui.interfaces

/**
 * Created by hlink53 on 16/2/16.
 */
interface OnItemClickListener<T> {
    fun onItemClicked(t: T)
    fun onDeleteClicked(t: T)

    fun onItemClicked(adapterPosition: T, checked: T)

    fun onDeleteClicked(adapterPosition: T, b: T)
}
