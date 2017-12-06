package com.mikepenz.fastadapter.utils;

import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The default item list implementation
 */

public class ComparableItemListImpl<Item extends IItem> extends DefaultItemList<Item> {

    private List<Item> mItems;

    private Comparator<Item> mComparator;

    public ComparableItemListImpl(Comparator<Item> comparator) {
        this.mItems = new ArrayList<>();
        this.mComparator = comparator;
    }

    public ComparableItemListImpl(Comparator<Item> comparator, List<Item> items) {
        this.mItems = items;
        this.mComparator = comparator;
    }

    @Override
    public Item get(int position) {
        return mItems.get(position);
    }

    @Override
    public List<Item> getItems() {
        return mItems;
    }

    @Override
    public int getAdapterPosition(long identifier) {
        for (int i = 0, size = mItems.size(); i < size; i++) {
            if (mItems.get(i).getIdentifier() == identifier) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void remove(int position, int preItemCount) {
        mItems.remove(position - preItemCount);
        getFastAdapter().notifyAdapterItemRemoved(position);
    }

    @Override
    public void removeRange(int position, int itemCount, int preItemCount) {
        //global position to relative
        int length = mItems.size();
        //make sure we do not delete to many items
        int saveItemCount = Math.min(itemCount, length - position + preItemCount);

        for (int i = 0; i < saveItemCount; i++) {
            mItems.remove(position - preItemCount);
        }
        getFastAdapter().notifyAdapterItemRangeRemoved(position, saveItemCount);
    }

    @Override
    public void move(int fromPosition, int toPosition, int preItemCount) {
        Item item = mItems.get(fromPosition - preItemCount);
        mItems.remove(fromPosition - preItemCount);
        mItems.add(toPosition - preItemCount, item);
        Collections.sort(mItems, mComparator);
        getFastAdapter().notifyAdapterDataSetChanged();
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Override
    public void clear(int preItemCount) {
        int size = mItems.size();
        mItems.clear();
        getFastAdapter().notifyAdapterItemRangeRemoved(preItemCount, size);
    }

    @Override
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Override
    public void set(int position, Item item) {
        mItems.set(position, item);
        getFastAdapter().notifyAdapterItemInserted(position);
    }

    @Override
    public void addAll(List<Item> items, int preItemCount) {
        mItems.addAll(items);
        Collections.sort(items, mComparator);
        getFastAdapter().notifyAdapterDataSetChanged();
    }

    @Override
    public void addAll(int position, List<Item> items, int preItemCount) {
        mItems.addAll(position - preItemCount, items);
        Collections.sort(items, mComparator);
        getFastAdapter().notifyAdapterDataSetChanged();
    }

    @Override
    public void setNewList(List<Item> items) {
        mItems = new ArrayList<>(items);
        getFastAdapter().notifyAdapterDataSetChanged();
    }
}