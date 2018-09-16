package com.billwiz.admin.billwiz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;



public class DrawerMonthViewRecyclerViewAdapter
        extends RecyclerView.Adapter<DrawerMonthViewRecyclerViewAdapter.viewHolder> {

    private ArrayList<Double> expenses;
    private ArrayList<Integer> records;
    private ArrayList<Integer> months;
    private ArrayList<Integer> years;

    private Context mContext;

    OnItemClickListener onItemClickListener;

    public DrawerMonthViewRecyclerViewAdapter(Context context) {
        mContext = context;
        expenses = new ArrayList<>();
        records = new ArrayList<>();
        months = new ArrayList<>();
        years = new ArrayList<>();

        if (RecordManager.getInstance(BillWizApplication.getAppContext()).RECORDS.size() != 0) {

            int currentYear = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.YEAR);
            int currentMonth = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.MONTH);
            double currentMonthSum = 0;
            int currentMonthRecord = 0;

            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR) == currentYear
                        && RecordManager.RECORDS.get(i).
                        getCalendar().get(Calendar.MONTH) == currentMonth) {
                    currentMonthSum += RecordManager.RECORDS.get(i).getMoney();
                    currentMonthRecord++;
                } else {
                    expenses.add(currentMonthSum);
                    records.add(currentMonthRecord);
                    years.add(currentYear);
                    months.add(currentMonth);
                    currentMonthSum = RecordManager.RECORDS.get(i).getMoney();
                    currentMonthRecord = 1;
                    currentYear = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR);
                    currentMonth = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.MONTH);
                }
            }
            expenses.add(currentMonthSum);
            records.add(currentMonthRecord);
            years.add(currentYear);
            months.add(currentMonth);

        }


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public viewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_month_view_drawer, parent, false);
        return new viewHolder(view) {
        };

    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        holder.month.setText(BillWizUtil.GetMonthShort(months.get(position) + 1));
        holder.month.setTypeface(BillWizUtil.getInstance().typefaceLatoLight);

        holder.year.setText(years.get(position) + "");
        holder.year.setTypeface(BillWizUtil.getInstance().typefaceLatoLight);

        holder.sum.setText(BillWizUtil.getInstance().GetInRecords(records.get(position)));
        holder.sum.setTypeface(BillWizUtil.getInstance().typefaceLatoLight);

        holder.money.setText(BillWizUtil.getInstance().GetInMoney((int) (double) (expenses.get(position))));
        holder.money.setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
    }

    public class viewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        @Optional
        @InjectView(R.id.month)
        TextView month;
        @Optional
        @InjectView(R.id.year)
        TextView year;
        @Optional
        @InjectView(R.id.money)
        TextView money;
        @Optional
        @InjectView(R.id.sum)
        TextView sum;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }

}
