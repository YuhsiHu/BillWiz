package com.billwiz.admin.billwiz.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.billwiz.admin.billwiz.adapter.MonthViewRecyclerViewAdapter;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MonthViewFragment extends Fragment {

    public int position;
    public int monthNumber;

    private List<BillWizRecord> list = new ArrayList<>();

    private Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private boolean IS_EMPTY = false;

    public static MonthViewFragment newInstance(int position, int monthNumber) {
        MonthViewFragment fragment = new MonthViewFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        args.putInt("MONTH_NUMBER", monthNumber);
        fragment.setArguments(args);
        fragment.monthNumber = monthNumber;
        fragment.position = position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        position = getArguments() != null ? getArguments().getInt("POSITION") : 1;
        monthNumber = getArguments() != null ? getArguments().getInt("MONTH_NUMBER") : 1;
        IS_EMPTY = (monthNumber == -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.month_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (!IS_EMPTY) {

            RecordManager recordManager = RecordManager.getInstance(mContext.getApplicationContext());

            int startYear = recordManager.RECORDS.get(0).getCalendar().get(Calendar.YEAR);
            int startMonth = recordManager.RECORDS.get(0).getCalendar().get(Calendar.MONTH);
            int nowYear = startYear + (startMonth + (monthNumber - position - 1)) / 12;
            int nowMonth = (startMonth + (monthNumber - position - 1)) % 12;

            Calendar monthStart = Calendar.getInstance();
            Calendar monthEnd = Calendar.getInstance();

            monthStart.set(nowYear, nowMonth, 1, 0, 0, 0);
            monthStart.add(Calendar.MILLISECOND, 0);

            monthEnd.set(
                    nowYear, nowMonth, monthStart.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
            monthEnd.add(Calendar.MILLISECOND, 0);

            Calendar leftRange = BillWizUtil.GetThisWeekLeftRange(monthStart);
            Calendar rightRange = BillWizUtil.GetThisWeekRightRange(monthEnd);

            int start = -1;
            int end = 0;

            for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                    end = i + 1;
                    break;
                } else if (recordManager.RECORDS.get(i).getCalendar().before(rightRange)) {
                    if (start == -1) {
                        start = i;
                    }
                }
            }

            mAdapter = new RecyclerViewMaterialAdapter(
                    new MonthViewRecyclerViewAdapter(start, end, mContext, position, monthNumber));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new RecyclerViewMaterialAdapter(
                    new MonthViewRecyclerViewAdapter(-1, -1, mContext, 0, -1));
            mRecyclerView.setAdapter(mAdapter);
        }

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
