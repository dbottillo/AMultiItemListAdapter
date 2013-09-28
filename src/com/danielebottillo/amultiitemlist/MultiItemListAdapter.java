package com.danielebottillo.amultiitemlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielebottillo on 9/23/13.
 */
public class MultiItemListAdapter implements WrapperListAdapter {

    private final ListAdapter originalAdapter;
    private final int numberOfColumns;
    private final int cellSpacing;
    private final Context context;
    private final LayoutParams singleElementLayoutParams;

    public MultiItemListAdapter(Context context, ListAdapter originalAdapter, int numberOfColumns, int cellSpacing) {
        this.context = context;
        this.originalAdapter = originalAdapter;
        this.numberOfColumns = numberOfColumns;
        this.cellSpacing = cellSpacing;

        // LayoutParams for the container layout, this view will contain the element of the original adapter
        singleElementLayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        singleElementLayoutParams.setMargins(cellSpacing, cellSpacing, 0, 0);
        singleElementLayoutParams.weight = 1;
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return originalAdapter;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return originalAdapter.areAllItemsEnabled();
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        originalAdapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        originalAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public boolean hasStableIds() {
        return originalAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int i) {
        return originalAdapter.getItemViewType(i);
    }

    @Override
    public int getViewTypeCount() {
        return originalAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return originalAdapter.isEnabled(i);
    }

    @Override
    public int getCount() {
        return (int) Math.ceil(1.0f * originalAdapter.getCount() / numberOfColumns);
    }

    @Override
    public Object getItem(int position) {
        // getItem return the array of element to display in every single row
        List<Object> items = new ArrayList<Object>(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            int p = position * numberOfColumns + i;
            if (p < originalAdapter.getCount()) {
                items.add(originalAdapter.getItem(p));
            }
        }
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LinearLayout linearLayout;

        if (convertView == null){
            // create a linear Layout
            linearLayout = new LinearLayout(context);
            linearLayout.setPadding(0, 0, cellSpacing, 0);
            linearLayout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBaselineAligned(false);
            linearLayout.setTag(position);
        } else {
            linearLayout = (LinearLayout) convertView;
        }

        for (int i=0; i< numberOfColumns; i++){
            View newView;

            int adapterPosition = position * numberOfColumns + i;

            View oldView = null;
            if (i < linearLayout.getChildCount()){
                oldView = linearLayout.getChildAt(i);
            }

            if (oldView != null) linearLayout.removeView(oldView);

            if (adapterPosition < originalAdapter.getCount()){
                if (oldView instanceof FakeView){
                    oldView = null;
                }
                newView = originalAdapter.getView(adapterPosition, oldView, linearLayout);
            }else{
                newView = new FakeView(context);
            }

            newView.setLayoutParams(singleElementLayoutParams);
            linearLayout.addView(newView, i);
        }
        return linearLayout;
    }

    protected static class FakeView extends View {
        public FakeView(Context context) {
            super(context);
        }
    }

}
