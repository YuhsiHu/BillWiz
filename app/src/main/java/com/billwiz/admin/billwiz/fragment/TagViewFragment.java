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
import com.billwiz.admin.billwiz.adapter.TagViewRecyclerViewAdapter;
import com.billwiz.admin.billwiz.model.BillWizRecord;
import com.billwiz.admin.billwiz.model.RecordManager;

import java.util.ArrayList;
import java.util.List;


public class TagViewFragment extends Fragment {

    private int position;

    private List<BillWizRecord> list = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Context mContext;

    public static TagViewFragment newInstance(int position) {
        TagViewFragment fragment = new TagViewFragment();
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
        return inflater.inflate(R.layout.account_book_fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (position == 0) {
            for (BillWizRecord billWizRecord : RecordManager.RECORDS) {
                list.add(billWizRecord);
            }
        } if (position == 1) {
            for (BillWizRecord billWizRecord : RecordManager.RECORDS) {
                list.add(billWizRecord);
            }
        } else {
            for (BillWizRecord billWizRecord : RecordManager.RECORDS) {
                if (billWizRecord.getTag() == RecordManager.TAGS.get(position).getId()) {
                    list.add(billWizRecord);
                }
            }
        }

        mAdapter = new RecyclerViewMaterialAdapter(new TagViewRecyclerViewAdapter(list, mContext, position));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
