package com.danielebottillo.amultiitemlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danielebottillo on 9/23/13.
 */
public class MultiItemListSectionAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = -1;
    private static final int TYPE_ELEMENT = 1;

    private final int numberOfColumns;
    private final int cellSpacing;
    private final Context context;
    private final LayoutParams singleElementLayoutParams;
    private final AbsListView.LayoutParams mRowLayoutParams;

    private ArrayList<MultiItemListHeader> headers = new ArrayList<MultiItemListHeader>();
    private Map<MultiItemListHeader, Adapter> sections = new HashMap<MultiItemListHeader, Adapter>();

    private LayoutInflater inflater;

    public MultiItemListSectionAdapter(Context context, int numberOfColumns, int cellSpacing) {
        this.context = context;

        this.numberOfColumns = numberOfColumns;
        this.cellSpacing = cellSpacing;

        singleElementLayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        singleElementLayoutParams.setMargins(cellSpacing, cellSpacing, 0, 0);
        singleElementLayoutParams.weight = 1;
        mRowLayoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        this.inflater = LayoutInflater.from(context);
    }

    public void addSection(MultiItemListHeader section, Adapter adapter){
        headers.add(section);
        sections.put(section, adapter);
    }

    @Override
    public int getCount() {
        int total = 0;
        for (Adapter adapter : this.sections.values()){
            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            total += totalSingleAdapter+1;
        }
        return total;
    }

    @Override
    public Object getItem(int position) {
        int previousAdapterSize = 0;
        List<Object> items = new ArrayList<Object>(numberOfColumns);

        for (MultiItemListHeader header : headers){
            if (position == previousAdapterSize) {
                return header;
            }

            Adapter adapter = sections.get(header);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            previousAdapterSize += totalSingleAdapter+1;

            if (position<previousAdapterSize) {
                int tempPrevious = position-1 - (previousAdapterSize-totalSingleAdapter-1);
                for (int i=0; i<numberOfColumns; i++){
                    int p = tempPrevious * numberOfColumns + i;

                    if (p<adapter.getCount()){
                        items.add(adapter.getItem(p));
                    }
                }

                return items;
            }
;
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position){
        for (MultiItemListHeader header : headers){
            Adapter adapter = sections.get(header);
            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            int size = totalSingleAdapter+1;

            if (position == 0) return TYPE_HEADER;
            if (position<size) return TYPE_ELEMENT;

            position -=size;
        }
        return TYPE_HEADER;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Adapter getAdapterOfPosition(int position){
        Adapter adapter = null;
        for (MultiItemListHeader header : headers){
            adapter = sections.get(header);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            int size = totalSingleAdapter+1;

            if (position < size) return adapter;

            position -= size;
        }
        return adapter;
    }

    private int getPositionOfAdapter(int position, int column){
        int previousAdapterSize = 0;
        for (MultiItemListHeader header : headers){
            if (position == previousAdapterSize) {
                throw new IllegalArgumentException("There is an header at that position");
            }

            Adapter adapter = sections.get(header);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            previousAdapterSize += totalSingleAdapter+1;

            if (position<previousAdapterSize) {
                int tempPrevious = position-1 - (previousAdapterSize-totalSingleAdapter-1);
                int p = tempPrevious * numberOfColumns + column;
                if (p < adapter.getCount()) {
                    return p;
                }else{
                    return -1;
                }
            }
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(position);

        if (type == TYPE_HEADER){

            MultiItemListHeader header = (MultiItemListHeader) getItem(position);
            convertView = header.getView();
            convertView.setTag(TYPE_HEADER);

            return convertView;
        }

        if (convertView != null && (Integer)convertView.getTag() == TYPE_HEADER){
            convertView = null;
        }

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

        Adapter originalAdapter = getAdapterOfPosition(position);
        for (int i=0; i< numberOfColumns; i++){
            View newView;

            int adapterPosition = getPositionOfAdapter(position, i);

            View oldView = null;
            if (i < linearLayout.getChildCount()){
                oldView = linearLayout.getChildAt(i);
            }

            if (oldView != null) linearLayout.removeView(oldView);

            if (adapterPosition < originalAdapter.getCount() && adapterPosition > -1){
                if (oldView instanceof MultiItemListAdapter.FakeView){
                    oldView = null;
                }
                newView = originalAdapter.getView(adapterPosition, oldView, linearLayout);
            }else{
                newView = new MultiItemListAdapter.FakeView(context);
            }

            newView.setLayoutParams(singleElementLayoutParams);
            linearLayout.addView(newView, i);
        }

        return linearLayout;
    }

}
