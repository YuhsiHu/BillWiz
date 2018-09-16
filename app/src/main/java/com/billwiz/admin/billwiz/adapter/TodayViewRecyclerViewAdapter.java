package com.billwiz.admin.billwiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.fragment.RecordCheckDialogFragment;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.model.SettingManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;



public class TodayViewRecyclerViewAdapter
        extends RecyclerView.Adapter<TodayViewRecyclerViewAdapter.viewHolder> {

    private OnItemClickListener onItemClickListener;

    private Context mContext;

    static final int TYPE_HEADER = 0;
    static final int TYPE_BODY = 1;

    static final int TODAY = 0;
    static final int YESTERDAY = 1;
    static final int THIS_WEEK = 2;
    static final int LAST_WEEK = 3;
    static final int THIS_MONTH = 4;
    static final int LAST_MONTH = 5;
    static final int THIS_YEAR = 6;
    static final int LAST_YEAR = 7;

    private int fragmentPosition;

    // the data of this fragment
    private ArrayList<BillWizRecord> allData;

    // store the sum of expenses of each tag
    private Map<Integer, Double> TagExpanse;
    // store the records of each tag
    private Map<Integer, List<BillWizRecord>> Expanse;
    // the original target value of the whole pie
    private float[] originalTargets;
    // whether the data of this fragment is empty
    private boolean IS_EMPTY;
    // the sum of the whole pie
    private double Sum;
    // the number of columns in the histogram
    private int columnNumber;
    // the axis date value of the histogram(hour, day of week and month, month)
    private int axis_date;
    // the month number
    private int month;

    // the selected position of one part of the pie
    private int pieSelectedPosition = 0;
    // the last selected position of one part of the pie
    private int lastPieSelectedPosition = -1;
    // the last selected position of one part of the histogram
    private int lastHistogramSelectedPosition = -1;

    // the date string on the footer and header
    private String dateString;
    // the date string shown in the dialog
    private String dateShownString;
    // the string shown in the dialog
    private String dialogTitle;

    // the selected tag in pie
    private int tagId = -1;
    // the selected column in histogram
    private int timeIndex;

    private MaterialDialog dialog;
    private View dialogView;

    public TodayViewRecyclerViewAdapter(int start, int end, Context context, int position) {

        mContext = context;
        fragmentPosition = position;
        Sum = 0;

        RecordManager recordManager = RecordManager.getInstance(mContext.getApplicationContext());

        allData = new ArrayList<>();
        if (start != -1)
            for (int i = start; i >= end; i--) allData.add(recordManager.RECORDS.get(i));

        IS_EMPTY = allData.isEmpty();

        setDateString();

        if (!IS_EMPTY) {
            if (fragmentPosition == TODAY || fragmentPosition == YESTERDAY) {
                columnNumber = 24;
                axis_date = Calendar.HOUR_OF_DAY;
            }
            if (fragmentPosition == THIS_WEEK || fragmentPosition == LAST_WEEK) {
                columnNumber = 7;
                axis_date = Calendar.DAY_OF_WEEK;
            }
            if (fragmentPosition == THIS_MONTH || fragmentPosition == LAST_MONTH) {
                columnNumber = allData.get(0).getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH);
                axis_date = Calendar.DAY_OF_MONTH;
            }
            if (fragmentPosition == THIS_YEAR || fragmentPosition == LAST_YEAR) {
                columnNumber = 12;
                axis_date = Calendar.MONTH;
            }

            TagExpanse = new TreeMap<>();
            Expanse = new HashMap<>();
            originalTargets = new float[columnNumber];
            for (int i = 0; i < columnNumber; i++) originalTargets[i] = 0;

            int size = recordManager.TAGS.size();
            for (int j = 2; j < size; j++) {
                TagExpanse.put(recordManager.TAGS.get(j).getId(), Double.valueOf(0));
                Expanse.put(recordManager.TAGS.get(j).getId(), new ArrayList<BillWizRecord>());
            }

            size = allData.size();
            for (int i = 0; i < size; i++) {
                BillWizRecord coCoinRecord = allData.get(i);
                TagExpanse.put(coCoinRecord.getTag(),
                        TagExpanse.get(coCoinRecord.getTag()) + Double.valueOf(coCoinRecord.getMoney()));
                Expanse.get(coCoinRecord.getTag()).add(coCoinRecord);
                Sum += coCoinRecord.getMoney();
                if (axis_date == Calendar.DAY_OF_WEEK) {
                    if (BillWizUtil.WEEK_START_WITH_SUNDAY)
                        originalTargets[coCoinRecord.getCalendar().get(axis_date) - 1]
                                += coCoinRecord.getMoney();
                    else originalTargets[(coCoinRecord.getCalendar().get(axis_date) + 5) % 7]
                                += coCoinRecord.getMoney();
                } else if (axis_date == Calendar.DAY_OF_MONTH) {
                    originalTargets[coCoinRecord.getCalendar().get(axis_date) - 1]
                            += coCoinRecord.getMoney();
                } else {
                    originalTargets[coCoinRecord.getCalendar().get(axis_date)]
                            += coCoinRecord.getMoney();
                }
            }

            TagExpanse = BillWizUtil.SortTreeMapByValues(TagExpanse);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (fragmentPosition == TODAY || fragmentPosition == YESTERDAY) {
            return position == 0 ? TYPE_HEADER : TYPE_BODY;
        }
        return TYPE_HEADER;
    }

    @Override
    public int getItemCount() {
        if (fragmentPosition == TODAY || fragmentPosition == YESTERDAY) {
            return allData.size() + 1;
        }
        return 1;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_today_view_head, parent, false);
                return new viewHolder(view) {
                };
            }
            case TYPE_BODY: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_today_view_body, parent, false);
                return new viewHolder(view) {
                };
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                holder.date.setText(dateString);
                holder.dateBottom.setText(dateString);
                holder.expanseSum.setText(BillWizUtil.GetInMoney((int) Sum));

                holder.date.setTypeface(BillWizUtil.GetTypeface());
                holder.dateBottom.setTypeface(BillWizUtil.GetTypeface());
                holder.expanseSum.setTypeface(BillWizUtil.typefaceLatoLight);

                if (IS_EMPTY) {
                    holder.emptyTip.setVisibility(View.VISIBLE);
                    holder.emptyTip.setText(BillWizUtil.GetTodayViewEmptyTip(fragmentPosition));
                    holder.emptyTip.setTypeface(BillWizUtil.GetTypeface());

                    holder.reset.setVisibility(View.GONE);

                    holder.pie.setVisibility(View.GONE);
                    holder.iconLeft.setVisibility(View.GONE);
                    holder.iconRight.setVisibility(View.GONE);

                    holder.histogram.setVisibility(View.GONE);
                    holder.histogram_icon_left.setVisibility(View.GONE);
                    holder.histogram_icon_right.setVisibility(View.GONE);
                    holder.all.setVisibility(View.GONE);
                    holder.dateBottom.setVisibility(View.GONE);
                } else {
                    holder.emptyTip.setVisibility(View.GONE);

                    final ArrayList<SliceValue> sliceValues = new ArrayList<>();

                    for (Map.Entry<Integer, Double> entry : TagExpanse.entrySet()) {
                        if (entry.getValue() >= 1) {
                            SliceValue sliceValue = new SliceValue(
                                    (float)(double)entry.getValue(),
                                    mContext.getApplicationContext().getResources().
                                            getColor(BillWizUtil.GetTagColorResource(entry.getKey())));
                            sliceValue.setLabel(String.valueOf(entry.getKey()));
                            sliceValues.add(sliceValue);
                        }
                    }

                    final PieChartData pieChartData = new PieChartData(sliceValues);

                    pieChartData.setHasLabels(false);
                    pieChartData.setHasLabelsOnlyForSelected(false);
                    pieChartData.setHasLabelsOutside(false);
                    pieChartData.setHasCenterCircle(SettingManager.getInstance().getIsHollow());

                    holder.pie.setPieChartData(pieChartData);
                    holder.pie.setChartRotationEnabled(false);

// two control button of pie////////////////////////////////////////////////////////////////////////
                    holder.iconRight.setVisibility(View.VISIBLE);
                    holder.iconRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lastPieSelectedPosition != -1) {
                                pieSelectedPosition = lastPieSelectedPosition;
                            }
                            pieSelectedPosition
                                    = (pieSelectedPosition - 1 + sliceValues.size())
                                    % sliceValues.size();
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            pieSelectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.pie.selectValue(selectedValue);
                        }
                    });
                    holder.iconLeft.setVisibility(View.VISIBLE);
                    holder.iconLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lastPieSelectedPosition != -1) {
                                pieSelectedPosition = lastPieSelectedPosition;
                            }
                            pieSelectedPosition
                                    = (pieSelectedPosition + 1)
                                    % sliceValues.size();
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            pieSelectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.pie.selectValue(selectedValue);
                        }
                    });

                    final List<Column> columns = new ArrayList<>();
                    final ColumnChartData columnChartData = new ColumnChartData(columns);

                    if (!(fragmentPosition == TODAY || fragmentPosition == YESTERDAY)) {


                        for (int i = 0; i < columnNumber; i++) {
                            if (lastHistogramSelectedPosition == -1 && originalTargets[i] == 0) {
                                lastHistogramSelectedPosition = i;
                            }
                            SubcolumnValue value = new SubcolumnValue(
                                    originalTargets[i], BillWizUtil.GetRandomColor());
                            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
                            subcolumnValues.add(value);
                            Column column = new Column(subcolumnValues);
                            column.setHasLabels(false);
                            column.setHasLabelsOnlyForSelected(false);
                            columns.add(column);
                        }

                        Axis axisX = new Axis();
                        List<AxisValue> axisValueList = new ArrayList<>();

                        for (int i = 0; i < columnNumber; i++) {
                            axisValueList.add(
                                    new AxisValue(i).setLabel(BillWizUtil.GetAxisDateName(axis_date, i)));
                        }

                        axisX.setValues(axisValueList);
                        Axis axisY = new Axis().setHasLines(true);

                        columnChartData.setAxisXBottom(axisX);
                        columnChartData.setAxisYLeft(axisY);
                        columnChartData.setStacked(true);

                        holder.histogram.setColumnChartData(columnChartData);
                        holder.histogram.setZoomEnabled(false);

                        // two control button of histogram//////////////////////////////////////////////////////////////////
                        holder.histogram_icon_left.setVisibility(View.VISIBLE);
                        holder.histogram_icon_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                do {
                                    lastHistogramSelectedPosition
                                            = (lastHistogramSelectedPosition - 1 + columnNumber)
                                            % columnNumber;
                                } while (columnChartData.getColumns()
                                        .get(lastHistogramSelectedPosition)
                                        .getValues().get(0).getValue() == 0);
                                SelectedValue selectedValue =
                                        new SelectedValue(
                                                lastHistogramSelectedPosition,
                                                0,
                                                SelectedValue.SelectedValueType.NONE);
                                holder.histogram.selectValue(selectedValue);
                            }
                        });
                        holder.histogram_icon_right.setVisibility(View.VISIBLE);
                        holder.histogram_icon_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                do {
                                    lastHistogramSelectedPosition
                                            = (lastHistogramSelectedPosition + 1)
                                            % columnNumber;
                                } while (columnChartData.getColumns()
                                        .get(lastHistogramSelectedPosition)
                                        .getValues().get(0).getValue() == 0);
                                SelectedValue selectedValue =
                                        new SelectedValue(
                                                lastHistogramSelectedPosition,
                                                0,
                                                SelectedValue.SelectedValueType.NONE);
                                holder.histogram.selectValue(selectedValue);
                            }
                        });
                    }

                    if (fragmentPosition == TODAY || fragmentPosition == YESTERDAY) {
                        holder.histogram_icon_left.setVisibility(View.INVISIBLE);
                        holder.histogram_icon_right.setVisibility(View.INVISIBLE);
                        holder.histogram.setVisibility(View.GONE);
                        holder.dateBottom.setVisibility(View.GONE);
                        holder.reset.setVisibility(View.GONE);
                    }

// set value touch listener of pie//////////////////////////////////////////////////////////////////
                    holder.pie.setOnValueTouchListener(new PieChartOnValueSelectListener() {
                        @Override
                        public void onValueSelected(int p, SliceValue sliceValue) {
                            // snack bar
                            RecordManager recordManager
                                    = RecordManager.getInstance(mContext.getApplicationContext());
                            String text;
                            tagId = Integer.valueOf(String.valueOf(sliceValue.getLabelAsChars()));
                            double percent = sliceValue.getValue() / Sum * 100;
                            if ("zh".equals(BillWizUtil.GetLanguage())) {
                                text = BillWizUtil.GetSpendString((int) sliceValue.getValue()) +
                                        BillWizUtil.GetPercentString(percent) + "\n" +
                                        "于" + BillWizUtil.GetTagName(tagId);
                            } else {
                                text = "Spend " + (int)sliceValue.getValue()
                                        + " (takes " + String.format("%.2f", percent) + "%)\n"
                                        + "in " + BillWizUtil.GetTagName(tagId);
                            }
                            if ("zh".equals(BillWizUtil.GetLanguage())) {
                                dialogTitle = dateShownString +
                                        BillWizUtil.GetSpendString((int) sliceValue.getValue()) + "\n" +
                                        "于" + BillWizUtil.GetTagName(tagId);
                            } else {
                                dialogTitle = "Spend " + (int)sliceValue.getValue()
                                        + dateShownString + "\n" +
                                        "in " + BillWizUtil.GetTagName(tagId);
                            }
                            Snackbar snackbar =
                                    Snackbar
                                            .with(mContext)
                                            .type(SnackbarType.MULTI_LINE)
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .position(Snackbar.SnackbarPosition.BOTTOM)
                                            .margin(15, 15)
                                            .backgroundDrawable(BillWizUtil.GetSnackBarBackground(
                                                    fragmentPosition - 2))
                                            .text(text)
                                            .textTypeface(BillWizUtil.GetTypeface())
                                            .textColor(Color.WHITE)
                                            .actionLabelTypeface(BillWizUtil.GetTypeface())
                                            .actionLabel(mContext.getResources()
                                                    .getString(R.string.check))
                                            .actionColor(Color.WHITE)
                                            .actionListener(new mActionClickListenerForPie());
                            SnackbarManager.show(snackbar);

                            if (p == lastPieSelectedPosition) {
                                return;
                            } else {
                                lastPieSelectedPosition = p;
                            }

                            if (!(fragmentPosition == TODAY || fragmentPosition == YESTERDAY)) {

// histogram data///////////////////////////////////////////////////////////////////////////////////
                                float[] targets = new float[columnNumber];
                                for (int i = 0; i < columnNumber; i++) targets[i] = 0;

                                for (int i = Expanse.get(tagId).size() - 1; i >= 0; i--) {
                                    BillWizRecord coCoinRecord = Expanse.get(tagId).get(i);
                                    if (axis_date == Calendar.DAY_OF_WEEK) {
                                        if (BillWizUtil.WEEK_START_WITH_SUNDAY) {
                                            targets[coCoinRecord.getCalendar().get(axis_date) - 1]
                                                    += coCoinRecord.getMoney();
                                        } else {
                                            targets[(coCoinRecord.getCalendar().get(axis_date) + 5) % 7]
                                                    += coCoinRecord.getMoney();
                                        }
                                    } else if (axis_date == Calendar.DAY_OF_MONTH) {
                                        targets[coCoinRecord.getCalendar().get(axis_date) - 1]
                                                += coCoinRecord.getMoney();
                                    } else {
                                        targets[coCoinRecord.getCalendar().get(axis_date)]
                                                += coCoinRecord.getMoney();
                                    }
                                }

                                lastHistogramSelectedPosition = -1;
                                for (int i = 0; i < columnNumber; i++) {
                                    if (lastHistogramSelectedPosition == -1 && targets[i] != 0) {
                                        lastHistogramSelectedPosition = i;
                                    }
                                    columnChartData.getColumns().
                                            get(i).getValues().get(0).setTarget(targets[i]);
                                }
                                holder.histogram.startDataAnimation();
                            }
                        }

                        @Override
                        public void onValueDeselected() {

                        }
                    });

                    if (!(fragmentPosition == TODAY || fragmentPosition == YESTERDAY)) {

// set value touch listener of histogram////////////////////////////////////////////////////////////
                        holder.histogram.setOnValueTouchListener(
                                new ColumnChartOnValueSelectListener() {
                            @Override
                            public void onValueSelected(int columnIndex,
                                                        int subcolumnIndex, SubcolumnValue value) {
                                lastHistogramSelectedPosition = columnIndex;
                                timeIndex = columnIndex;
                                // snack bar
                                RecordManager recordManager
                                        = RecordManager.getInstance(mContext.getApplicationContext());

                                String text = BillWizUtil.GetSpendString((int) value.getValue());
                                if (tagId != -1)
                                    // belongs a tag
                                    if ("zh".equals(BillWizUtil.GetLanguage()))
                                        text = getSnackBarDateString() + text + "\n" +
                                                "于" + BillWizUtil.GetTagName(tagId);
                                    else
                                        text += getSnackBarDateString() + "\n"
                                                + "in " + BillWizUtil.GetTagName(tagId);
                                else
                                    // don't belong to any tag
                                    if ("zh".equals(BillWizUtil.GetLanguage()))
                                        text = getSnackBarDateString() + "\n" + text;
                                    else
                                        text += "\n" + getSnackBarDateString();

// setting the snack bar and dialog title of histogram//////////////////////////////////////////////
                                dialogTitle = text;
                                Snackbar snackbar =
                                        Snackbar
                                                .with(mContext)
                                                .type(SnackbarType.MULTI_LINE)
                                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                                .margin(15, 15)
                                                .backgroundDrawable(BillWizUtil.GetSnackBarBackground(
                                                        fragmentPosition - 2))
                                                .text(text)
                                                .textTypeface(BillWizUtil.GetTypeface())
                                                .textColor(Color.WHITE)
                                                .actionLabelTypeface(BillWizUtil.GetTypeface())
                                                .actionLabel(mContext.getResources()
                                                        .getString(R.string.check))
                                                .actionColor(Color.WHITE)
                                                .actionListener(new mActionClickListenerForHistogram());
                                SnackbarManager.show(snackbar);
                            }

                            @Override
                            public void onValueDeselected() {

                            }
                        });
                    }

// set the listener of the reset button/////////////////////////////////////////////////////////////
                    if (!(fragmentPosition == TODAY || fragmentPosition == YESTERDAY)) {
                        holder.reset.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tagId = -1;
                                lastHistogramSelectedPosition = -1;

                                for (int i = 0; i < columnNumber; i++) {
                                    if (lastHistogramSelectedPosition == -1
                                            && originalTargets[i] != 0) {
                                        lastHistogramSelectedPosition = i;
                                    }
                                    columnChartData.getColumns().
                                            get(i).getValues().get(0).setTarget(originalTargets[i]);
                                }

                                holder.histogram.startDataAnimation();
                            }
                        });
                    }

// set the listener of the show all button//////////////////////////////////////////////////////////
                    holder.all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((FragmentActivity)mContext).getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(new RecordCheckDialogFragment(
                                            mContext, allData, getAllDataDialogTitle()), "MyDialog")
                                    .commit();
                        }
                    });

                }

                break;

            case TYPE_BODY:

                holder.tagImage.setImageResource(
                        BillWizUtil.GetTagIcon(allData.get(position - 1).getTag()));
                holder.money.setText((int) allData.get(position - 1).getMoney() + "");
                holder.money.setTypeface(BillWizUtil.typefaceLatoLight);
                holder.cell_date.setText(allData.get(position - 1).getCalendarString());
                holder.cell_date.setTypeface(BillWizUtil.typefaceLatoLight);
                holder.remark.setText(allData.get(position - 1).getRemark());
                holder.remark.setTypeface(BillWizUtil.typefaceLatoLight);
                holder.index.setText(position + "");
                holder.index.setTypeface(BillWizUtil.typefaceLatoLight);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String subTitle;
                        double spend = allData.get(position - 1).getMoney();
                        int tagId = allData.get(position - 1).getTag();
                        if ("zh".equals(BillWizUtil.GetLanguage())) {
                            subTitle = BillWizUtil.GetSpendString((int)spend) +
                                    "于" + BillWizUtil.GetTagName(tagId);
                        } else {
                            subTitle = "Spend " + (int)spend +
                                    "in " + BillWizUtil.GetTagName(tagId);
                        }
                        dialog = new MaterialDialog.Builder(mContext)
                                .icon(BillWizUtil.GetTagIconDrawable(allData.get(position - 1).getTag()))
                                .limitIconToDefaultSize()
                                .title(subTitle)
                                .customView(R.layout.dialog_a_record, true)
                                .positiveText(R.string.get)
                                .show();
                        dialogView = dialog.getCustomView();
                        TextView remark = (TextView)dialogView.findViewById(R.id.remark);
                        TextView date = (TextView)dialogView.findViewById(R.id.date);
                        remark.setText(allData.get(position - 1).getRemark());
                        date.setText(allData.get(position - 1).getCalendarString());
                    }
                });

                break;
        }
    }

// view holder class////////////////////////////////////////////////////////////////////////////////
    public static class viewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.date)
        TextView date;
        @Optional
        @InjectView(R.id.date_bottom)
        TextView dateBottom;
        @Optional
        @InjectView(R.id.expanse)
        TextView expanseSum;
        @Optional
        @InjectView(R.id.empty_tip)
        TextView emptyTip;
        @Optional
        @InjectView(R.id.chart_pie)
        PieChartView pie;
        @Optional
        @InjectView(R.id.histogram)
        ColumnChartView histogram;
        @Optional
        @InjectView(R.id.icon_left)
        MaterialIconView iconLeft;
        @Optional
        @InjectView(R.id.icon_right)
        MaterialIconView iconRight;
        @Optional
        @InjectView(R.id.histogram_icon_left)
        MaterialIconView histogram_icon_left;
        @Optional
        @InjectView(R.id.histogram_icon_right)
        MaterialIconView histogram_icon_right;
        @Optional
        @InjectView(R.id.icon_reset)
        MaterialIconView reset;
        @Optional
        @InjectView(R.id.all)
        MaterialIconView all;
        @Optional
        @InjectView(R.id.tag_image)
        ImageView tagImage;
        @Optional
        @InjectView(R.id.money)
        TextView money;
        @Optional
        @InjectView(R.id.cell_date)
        TextView cell_date;
        @Optional
        @InjectView(R.id.remark)
        TextView remark;
        @Optional
        @InjectView(R.id.index)
        TextView index;
        @Optional
        @InjectView(R.id.material_ripple_layout)
        MaterialRippleLayout layout;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

// set the listener of the check button on the snack bar of pie/////////////////////////////////////
    private class mActionClickListenerForPie implements ActionClickListener {
        @Override
        public void onActionClicked(Snackbar snackbar) {
            List<BillWizRecord> shownCoCoinRecords = Expanse.get(tagId);
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialogFragment(
                            mContext, shownCoCoinRecords, dialogTitle), "MyDialog")
                    .commit();
        }
    }

// set the listener of the check button on the snack bar of histogram///////////////////////////////
    private class mActionClickListenerForHistogram implements ActionClickListener {
        @Override
        public void onActionClicked(Snackbar snackbar) {
            ArrayList<BillWizRecord> shownCoCoinRecords = new ArrayList<>();
            int index = timeIndex;
            if (axis_date == Calendar.DAY_OF_WEEK) {
                if (BillWizUtil.WEEK_START_WITH_SUNDAY) index++;
                else
                    if (index == 6) index = 1;
                    else index += 2;
            }
            if (fragmentPosition == THIS_MONTH || fragmentPosition == LAST_MONTH) index++;
            if (tagId != -1) {
                for (int i = 0; i < Expanse.get(tagId).size(); i++)
                    if (Expanse.get(tagId).get(i).getCalendar().get(axis_date) == index)
                        shownCoCoinRecords.add(Expanse.get(tagId).get(i));
            } else {
                for (int i = 0; i < allData.size(); i++)
                    if (allData.get(i).getCalendar().get(axis_date) == index)
                        shownCoCoinRecords.add(allData.get(i));
            }
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialogFragment(
                            mContext, shownCoCoinRecords, dialogTitle), "MyDialog")
                    .commit();
        }
    }

// set the dateString shown in snack bar in this fragment///////////////////////////////////////////
    private String getSnackBarDateString() {
        switch (fragmentPosition) {
            case TODAY:
                if ("zh".equals(BillWizUtil.GetLanguage()))
                    // 在今天9点
                    return mContext.getResources().getString(R.string.at) +
                            mContext.getResources().getString(R.string.today_date_string) +
                            timeIndex +
                            mContext.getResources().getString(R.string.o_clock);
                else
                    // at 9 o'clock today
                    return mContext.getResources().getString(R.string.at) +
                            timeIndex + " " +
                            mContext.getResources().getString(R.string.o_clock) + " " +
                            mContext.getResources().getString(R.string.today_date_string);
            case YESTERDAY:
                if ("zh".equals(BillWizUtil.GetLanguage()))
                    // 在昨天9点
                    return mContext.getResources().getString(R.string.at) +
                            mContext.getResources().getString(R.string.yesterday_date_string) +
                            timeIndex +
                            mContext.getResources().getString(R.string.o_clock);
                else
                    // at 9 o'clock yesterday
                    return mContext.getResources().getString(R.string.at) +
                            timeIndex + " " +
                            mContext.getResources().getString(R.string.o_clock) + " " +
                            mContext.getResources().getString(R.string.yesterday_date_string);
            case THIS_WEEK:
                // 在周一
                // on Monday
                return mContext.getResources().getString(R.string.on)
                        + BillWizUtil.GetWeekDay(timeIndex);
            case LAST_WEEK:
                // 在上周一
                // on last Monday
                return mContext.getResources().getString(R.string.on)
                        + mContext.getResources().getString(R.string.last)
                        + BillWizUtil.GetWeekDay(timeIndex);
            case THIS_MONTH:
                // 在1月1日
                // on Jan. 1
                return mContext.getResources().getString(R.string.on) +
                        BillWizUtil.GetMonthShort(month) + BillWizUtil.GetWhetherBlank() +
                        (timeIndex + 1) + BillWizUtil.GetWhetherFuck();
            case LAST_MONTH:
                // 在1月1日
                // on Jan. 1
                return mContext.getResources().getString(R.string.on) +
                        BillWizUtil.GetMonthShort(month) + BillWizUtil.GetWhetherBlank() +
                        (timeIndex + 1) + BillWizUtil.GetWhetherFuck();
            case THIS_YEAR:
                if ("zh".equals(BillWizUtil.GetLanguage()))
                    // 在今年1月
                    return mContext.getResources().getString(R.string.in) +
                            mContext.getResources().getString(R.string.this_year_date_string) +
                            BillWizUtil.GetMonthShort(timeIndex + 1);
                else
                    // in Jan. 1
                    return mContext.getResources().getString(R.string.in) +
                            BillWizUtil.GetMonthShort(timeIndex + 1) + " " +
                            mContext.getResources().getString(R.string.this_year_date_string);
            case LAST_YEAR:
                if ("zh".equals(BillWizUtil.GetLanguage()))
                    // 在去年1月
                    return mContext.getResources().getString(R.string.in) +
                            mContext.getResources().getString(R.string.last_year_date_string) +
                            BillWizUtil.GetMonthShort(timeIndex + 1);
                else
                    // in Jan. 1
                    return mContext.getResources().getString(R.string.in) +
                            BillWizUtil.GetMonthShort(timeIndex + 1) + " " +
                            mContext.getResources().getString(R.string.last_year_date_string);
            default:
                return "";
        }
    }

// set the dateString of this fragment//////////////////////////////////////////////////////////////
    private void setDateString() {
        String basicTodayDateString;
        String basicYesterdayDateString;
        Calendar today = Calendar.getInstance();
        Calendar yesterday = BillWizUtil.GetYesterdayLeftRange(today);
        basicTodayDateString = "--:-- ";
        basicTodayDateString += BillWizUtil.GetMonthShort(today.get(Calendar.MONTH) + 1)
                + " " + today.get(Calendar.DAY_OF_MONTH) + " " +
                today.get(Calendar.YEAR);
        basicYesterdayDateString = "--:-- ";
        basicYesterdayDateString += BillWizUtil.GetMonthShort(today.get(Calendar.MONTH) + 1)
                + " " + yesterday.get(Calendar.DAY_OF_MONTH) + " " +
                yesterday.get(Calendar.YEAR);
        switch (fragmentPosition) {
            case TODAY:
                dateString = basicTodayDateString.substring(6, basicTodayDateString.length());
                dateShownString = mContext.getResources().getString(R.string.today_date_string);
                month = today.get(Calendar.MONTH);
                break;
            case YESTERDAY:
                dateString
                        = basicYesterdayDateString.substring(6, basicYesterdayDateString.length());
                dateShownString = mContext.getResources().getString(R.string.yesterday_date_string);
                month = yesterday.get(Calendar.MONTH);
                break;
            case THIS_WEEK:
                Calendar leftWeekRange = BillWizUtil.GetThisWeekLeftRange(today);
                Calendar rightWeekRange = BillWizUtil.GetThisWeekRightShownRange(today);
                dateString = BillWizUtil.GetMonthShort(leftWeekRange.get(Calendar.MONTH) + 1)
                        + " " + leftWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        leftWeekRange.get(Calendar.YEAR) + " - " +
                        BillWizUtil.GetMonthShort(rightWeekRange.get(Calendar.MONTH) + 1)
                        + " " + rightWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        rightWeekRange.get(Calendar.YEAR);
                dateShownString = mContext.getResources().getString(R.string.this_week_date_string);
                month = -1;
                break;
            case LAST_WEEK:
                Calendar leftLastWeekRange = BillWizUtil.GetLastWeekLeftRange(today);
                Calendar rightLastWeekRange = BillWizUtil.GetLastWeekRightShownRange(today);
                dateString
                        = BillWizUtil.GetMonthShort(leftLastWeekRange.get(Calendar.MONTH) + 1)
                        + " " + leftLastWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        leftLastWeekRange.get(Calendar.YEAR) + " - " +
                        BillWizUtil.GetMonthShort(rightLastWeekRange.get(Calendar.MONTH) + 1)
                        + " " + rightLastWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        rightLastWeekRange.get(Calendar.YEAR);
                dateShownString = mContext.getResources().getString(R.string.last_week_date_string);
                month = -1;
                break;
            case THIS_MONTH:
                dateString = BillWizUtil.GetMonthShort(today.get(Calendar.MONTH) + 1)
                        + " " + today.get(Calendar.YEAR);
                dateShownString
                        = mContext.getResources().getString(R.string.this_month_date_string);
                month = today.get(Calendar.MONTH);
                break;
            case LAST_MONTH:
                Calendar lastMonthCalendar = BillWizUtil.GetLastMonthLeftRange(today);
                dateString
                        = BillWizUtil.GetMonthShort(lastMonthCalendar.get(Calendar.MONTH) + 1)
                        + " " + lastMonthCalendar.get(Calendar.YEAR);
                dateShownString
                        = mContext.getResources().getString(R.string.last_month_date_string);
                month = lastMonthCalendar.get(Calendar.MONTH);
                break;
            case THIS_YEAR:
                dateString = today.get(Calendar.YEAR) + "";
                dateShownString = mContext.getResources().getString(R.string.this_year_date_string);
                month = -1;
                break;
            case LAST_YEAR:
                Calendar lastYearCalendar = BillWizUtil.GetLastYearLeftRange(today);
                dateString = lastYearCalendar.get(Calendar.YEAR) + "";
                dateShownString = mContext.getResources().getString(R.string.last_year_date_string);
                month = -1;
                break;
        }
    }

    private String getAllDataDialogTitle() {
        String prefix;
        String postfix;
        if ("zh".equals(BillWizUtil.GetLanguage())) {
            prefix = mContext.getResources().getString(R.string.on);
            postfix = BillWizUtil.GetSpendString((int)Sum);
        } else {
            prefix = BillWizUtil.GetSpendString((int)Sum);
            postfix = "";
        }
        switch (fragmentPosition) {
            case TODAY:
                return prefix + mContext.getResources().
                        getString(R.string.today_date_string) + postfix;
            case YESTERDAY:
                return prefix + mContext.getResources().
                        getString(R.string.yesterday_date_string) + postfix;
            case THIS_WEEK:
                return prefix + mContext.getResources().
                        getString(R.string.this_week_date_string) + postfix;
            case LAST_WEEK:
                return prefix + mContext.getResources().
                        getString(R.string.last_week_date_string) + postfix;
            case THIS_MONTH:
                return prefix + mContext.getResources().
                        getString(R.string.this_month_date_string) + postfix;
            case LAST_MONTH:
                return prefix + mContext.getResources().
                        getString(R.string.last_month_date_string) + postfix;
            case THIS_YEAR:
                return prefix + mContext.getResources().
                        getString(R.string.this_year_date_string) + postfix;
            case LAST_YEAR:
                return prefix + mContext.getResources().
                        getString(R.string.last_year_date_string) + postfix;
            default:
                return "";
        }
    }

}
