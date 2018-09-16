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
import com.billwiz.admin.billwiz.adapter.TodayViewRecyclerViewAdapter;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TodayViewFragment extends Fragment {

    private int position;

    private List<BillWizRecord> list = new ArrayList<>();

    private Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerViewMaterialAdapter mAdapter;
    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    static final int TODAY = 0;
    static final int YESTERDAY = 1;
    static final int THIS_WEEK = 2;
    static final int LAST_WEEK = 3;
    static final int THIS_MONTH = 4;
    static final int LAST_MONTH = 5;
    static final int THIS_YEAR = 6;
    static final int LAST_YEAR = 7;

    public static TodayViewFragment newInstance(int position) {
        TodayViewFragment fragment = new TodayViewFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        position = getArguments() != null ? getArguments().getInt("POSITION") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Calendar now = Calendar.getInstance();
        Calendar leftRange;
        Calendar rightRange;

        RecordManager recordManager = RecordManager.getInstance(mContext.getApplicationContext());
        int start = -1;
        int end = 0;

        switch (position) {
            case TODAY:
                leftRange = BillWizUtil.GetTodayLeftRange(now);
                for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        end = i + 1;
                        break;
                    }
                    if (start == -1) {
                        start = i;
                    }
                }
                break;
            case YESTERDAY:
                leftRange = BillWizUtil.GetYesterdayLeftRange(now);
                rightRange = BillWizUtil.GetYesterdayRightRange(now);
                for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        end = i + 1;
                        break;
                    } else if (!recordManager.RECORDS.get(i).getCalendar().after(rightRange)) {
                        if (start == -1) {
                            start = i;
                        }
                    }
                }
                break;
            case THIS_WEEK:
                leftRange = BillWizUtil.GetThisWeekLeftRange(now);
                for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        end = i + 1;
                        break;
                    }
                    if (start == -1) {
                        start = i;
                    }
                }
                break;
            case LAST_WEEK:
                leftRange = BillWizUtil.GetLastWeekLeftRange(now);
                rightRange = BillWizUtil.GetLastWeekRightRange(now);
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
                break;
            case THIS_MONTH:
                leftRange = BillWizUtil.GetThisMonthLeftRange(now);
                for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        end = i + 1;
                        break;
                    }
                    if (start == -1) {
                        start = i;
                    }
                }
                break;
            case LAST_MONTH:
                leftRange = BillWizUtil.GetLastMonthLeftRange(now);
                rightRange = BillWizUtil.GetLastMonthRightRange(now);
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
                break;
            case THIS_YEAR:
                leftRange = BillWizUtil.GetThisYearLeftRange(now);
                for (int i = recordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (recordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        end = i + 1;
                        break;
                    }
                    if (start == -1) {
                        start = i;
                    }
                }
                break;
            case LAST_YEAR:
                leftRange = BillWizUtil.GetLastYearLeftRange(now);
                rightRange = BillWizUtil.GetLastYearRightRange(now);
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
                break;
        }

        adapter = new TodayViewRecyclerViewAdapter(start, end, mContext, position);

        mAdapter = new RecyclerViewMaterialAdapter(adapter);
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

}
