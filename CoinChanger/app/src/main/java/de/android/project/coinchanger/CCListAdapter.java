package de.android.project.coinchanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank
 * 20.05.2015.
 */
public class CCListAdapter extends BaseAdapter {

    private Context context;
    private List<CurrencyItem> itemList;
    private CurrencyItem currentItem;
    private float currencyValue;

    public void setCurrentCurrencyItem(CurrencyItem item) {
        this.currentItem = item;
        notifyDataSetChanged();
    }

    public void setCurrencyValue(float value) {
        this.currencyValue = value;
        notifyDataSetChanged();
    }

    public CCListAdapter(Context context, List<CurrencyItem> itemList, CurrencyItem currentItem) {
        this.context = context;
        this.itemList = itemList;
        this.currentItem = currentItem;
    }


    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (itemList == null || itemList.size() == 0) {
            return null;
        }

        CurrencyItem currencyItem = itemList.get(position);
        View recordRow = convertView;
        EventListItemHolder holder;

        if (recordRow == null) {

            recordRow = LayoutInflater.from(context).inflate(R.layout.row_listview, parent, false);

            holder = new EventListItemHolder(recordRow, currentItem);
            recordRow.setTag(holder);

        } else {
            holder = (EventListItemHolder) recordRow.getTag();
        }

        holder.populateFrom(currencyItem);


        return recordRow;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    public void refillAdapter(List<CurrencyItem> newList) {
        itemList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    private class EventListItemHolder {

        // //////////////////////////////////////////////////////////
        // Variables
        // //////////////////////////////////////////////////////////

        private View row;
        private TextView beginday;
        private TextView beginmonth;
        private CurrencyItem baseItem;

        // //////////////////////////////////////////////////////////
        // Constructor
        // //////////////////////////////////////////////////////////

        public EventListItemHolder(View row, CurrencyItem baseItem) {
            this.row = row;
            this.baseItem = baseItem;
        }

        // //////////////////////////////////////////////////////////
        // Getters and Setters
        // //////////////////////////////////////////////////////////


        public TextView getBeginday() {

            if (beginday == null) {
                beginday = (TextView) row.findViewById(R.id.row_name);
            }
            return beginday;
        }

        public TextView getBeginmonth() {

            if (beginmonth == null) {
                beginmonth = (TextView) row.findViewById(R.id.row_value);
            }
            return beginmonth;
        }

        // //////////////////////////////////////////////////////////
        // Methods
        // //////////////////////////////////////////////////////////
        public void populateFrom(CurrencyItem item) {

            TextView name = getBeginday();
            if (name != null) {


                if (item.getFullName() != null) {
                    name.setText(item.getFullName());
                } else {
                    name.setText(item.getName());
                }

                TextView value = getBeginmonth();
                if (value != null) {
                    float base = (currencyValue / currentItem.getValue()) * item.getValue();
                    double result = (double) Math.round(base * 100) / 100;
                    value.setText(Double.toString(result));
                }
            }
        }
    }
}
