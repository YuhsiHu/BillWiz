package com.billwiz.admin.billwiz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RecordCheckDialogRecyclerViewAdapter extends RecyclerView.Adapter<RecordCheckDialogRecyclerViewAdapter.viewHolder> {

    private OnItemClickListener onItemClickListener;

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<BillWizRecord> billWizRecords;

    public RecordCheckDialogRecyclerViewAdapter(Context context, List<BillWizRecord> list) {
        billWizRecords = new ArrayList<>();
        billWizRecords = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public RecordCheckDialogRecyclerViewAdapter(Context context, List<BillWizRecord> list, OnItemClickListener onItemClickListener) {
        billWizRecords = new ArrayList<>();
        billWizRecords = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(mLayoutInflater.inflate(R.layout.record_check_item, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.imageView.setImageResource(
                BillWizUtil.GetTagIcon(billWizRecords.get(position).getTag()));
        holder.date.setText(billWizRecords.get(position).getCalendarString());
        holder.date.setTypeface(BillWizUtil.typefaceLatoLight);
        holder.money.setTypeface(BillWizUtil.typefaceLatoLight);
        holder.money.setText(String.valueOf((int) billWizRecords.get(position).getMoney()));
        holder.money.setTextColor(
                BillWizUtil.GetTagColorResource(RecordManager.TAGS.get(billWizRecords.get(position).getTag()).getId()));
        holder.index.setText((position + 1) + "");
        holder.index.setTypeface(BillWizUtil.typefaceLatoLight);
        holder.remark.setText(billWizRecords.get(position).getRemark());
        holder.remark.setTypeface(BillWizUtil.typefaceLatoLight);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (billWizRecords == null) {
            return 0;
        }
        return billWizRecords.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.image_view)
        ImageView imageView;
        @InjectView(R.id.date)
        TextView date;
        @InjectView(R.id.remark)
        TextView remark;
        @InjectView(R.id.money)
        TextView money;
        @InjectView(R.id.index)
        TextView index;
        @InjectView(R.id.material_ripple_layout)
        MaterialRippleLayout layout;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @Override
        public void onClick(View v) {
//            onItemClickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}