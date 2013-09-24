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
        //STM.LogMessage("total get count multi item " + total);
        return total;
    }

    @Override
    public Object getItem(int position) {
        int previousAdapterSize = 0;
        List<Object> items = new ArrayList<Object>(numberOfColumns);
        //int lastSize = 0;
        for (MultiItemListHeader header : headers){
            if (position == previousAdapterSize) {
                return header;
            }

            Adapter adapter = sections.get(header);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            previousAdapterSize += totalSingleAdapter+1;

            if (position<previousAdapterSize) {
                //int realPosition =
                //int adapterPosition = (position-1) * numberOfColumns + row;
                //ArrayList<STMPost> items = getItem(position);

                /*STM.LogMessage("index "+column+" of columns "+numberOfColumns+" with tempPrevious "+tempPrevious);
                int p = tempPrevious * numberOfColumns + column;
                STM.LogMessage("The position inside the adapter items is "+p);
                if (p < adapter.getCount()) {
                    return p;
                }else{
                    return -1;
                }*/

                int tempPrevious = position-1 - (previousAdapterSize-totalSingleAdapter-1);
                for (int i=0; i<numberOfColumns; i++){
                    int p = tempPrevious * numberOfColumns + i;

                    if (p<adapter.getCount()){
                        items.add(adapter.getItem(p));
                    }
                }

                return items;
                /*for (int i = 0; i < numberOfColumns; i++) {
                    STM.LogMessage("index "+i+" of columns "+numberOfColumns+" with tempPrevious "+tempPrevious);
                    int p = tempPrevious * numberOfColumns + i;
                    STM.LogMessage("The position inside the adapter items is "+p);

                    if (p < adapter.getCount()) {
                        return p;
                    }else{
                        return -1;
                    }
                }*/
            }

            //position -=size;
        }
        return -1;
        //STM.LogMessage("getItem at position "+position);
/*
        //List<Object> items = new ArrayList<Object>(numberOfColumns);
        //int previousAdapterSize = 0;

        //int realPosition = position;
        for (String header : headers){
            Adapter adapter = sections.get(header);
           // STM.LogMessage("current adapter checking: "+adapter);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);

            int size = totalSingleAdapter+1;

            if (position == 0) {
                //STM.LogMessage("it's an header position");
                return header;
            }
            /*if (position<previousAdapterSize) {
                //int realPos = position-
                int tempPrevious = position-1;
                for (int i = 0; i < numberOfColumns; i++) {
                    //STM.LogMessage("index "+i+" of columns "+numberOfColumns+" with tempPrevious "+tempPrevious);
                    int p = tempPrevious * numberOfColumns + i;
                    //STM.LogMessage("I need to add "+p);
                    if (p < adapter.getCount()) {
                        items.add(adapter.getItem(p));
                    }
                }
            }*//*

            //realPosition -= previousAdapterSize;
            position -=size;
        }
        return null;
    */

    }

    public Adapter getAdapterOfPosition(int position){
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

    public int getPositionOfAdapter(int position, int column){
        //STM.LogMessage("getPositionOfAdapter "+position+" with column "+column);
        int previousAdapterSize = 0;
        //int lastSize = 0;
        for (MultiItemListHeader header : headers){
            if (position == previousAdapterSize) {
                throw new IllegalArgumentException("There is an header at that position");
            }

            Adapter adapter = sections.get(header);
            //STM.LogMessage("current adapter checking: "+adapter);

            int totalSingleAdapter = (int) Math.ceil(1.0f * adapter.getCount() / numberOfColumns);
            previousAdapterSize += totalSingleAdapter+1;

            //lastSize = previousAdapterSize;

            if (position<previousAdapterSize) {
                //int realPosition =
                //int adapterPosition = (position-1) * numberOfColumns + row;
                //ArrayList<STMPost> items = getItem(position);
                int tempPrevious = position-1 - (previousAdapterSize-totalSingleAdapter-1);
                //STM.LogMessage("index "+column+" of columns "+numberOfColumns+" with tempPrevious "+tempPrevious);
                int p = tempPrevious * numberOfColumns + column;
                //STM.LogMessage("The position inside the adapter items is "+p);
                if (p < adapter.getCount()) {
                    return p;
                }else{
                    return -1;
                }
                /*for (int i = 0; i < numberOfColumns; i++) {
                    STM.LogMessage("index "+i+" of columns "+numberOfColumns+" with tempPrevious "+tempPrevious);
                    int p = tempPrevious * numberOfColumns + i;
                    STM.LogMessage("The position inside the adapter items is "+p);

                    if (p < adapter.getCount()) {
                        return p;
                    }else{
                        return -1;
                    }
                }*/
            }

            //position -=size;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(position);

        //STM.LogMessage("type view: "+type+" at row: "+position);

        if (type == TYPE_HEADER){
            //STM.LogMessage("header");

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
            //linearLayout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 200));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBaselineAligned(false);
            linearLayout.setTag(position);
        } else {
            linearLayout = (LinearLayout) convertView;
        }

        //if (position % 2 == 0) linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        //else linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));

        Adapter originalAdapter = getAdapterOfPosition(position);
        //STM.LogMessage("originalAdapter: "+originalAdapter+" items: "+getItem(position));
        for (int i=0; i< numberOfColumns; i++){
            View newView;

            int adapterPosition = (position-1) * numberOfColumns + i;
            adapterPosition = getPositionOfAdapter(position, i);

            View oldView = null;
            if (i < linearLayout.getChildCount()){
                oldView = linearLayout.getChildAt(i);
            }

            if (oldView != null) linearLayout.removeView(oldView);

            //STM.LogMessage("position "+position+" of total "+getCount());
            if (adapterPosition < originalAdapter.getCount() && adapterPosition > -1){
                if (oldView instanceof FakeView){
                    oldView = null;
                }
                newView = originalAdapter.getView(adapterPosition, oldView, linearLayout);
                newView.setLayoutParams(singleElementLayoutParams);
                //STM.LogMessage("i'm adding element at index"+i);
                linearLayout.addView(newView, i);
            }else{
                FakeView fakeView = new FakeView(context);
                //fakeView.setBackgroundColor(mContextReference.get().getResources().getColor(android.R.color.holo_red_dark));
                fakeView.setLayoutParams(singleElementLayoutParams);
                //STM.LogMessage("i'm adding fake at index "+i);
                linearLayout.addView(fakeView, i);
            }
        }

        return linearLayout;
    }

    private static class FakeView extends View {
        public FakeView(Context context) {
            super(context);
        }
    }

}
