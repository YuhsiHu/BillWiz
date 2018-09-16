package com.billwiz.admin.billwiz.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.adapter.TagChooseGridViewAdapter;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.ui.MyGridView;



public class TagChooseFragment extends Fragment {

    public TagChooseGridViewAdapter getTagAdapter() {
        return tagAdapter;
    }

    public void setTagAdapter(TagChooseGridViewAdapter tagAdapter) {
        this.tagAdapter = tagAdapter;
    }

    private TagChooseGridViewAdapter tagAdapter;
    private int fragmentPosition;
    public MyGridView myGridView;

    Activity activity;

    static public TagChooseFragment newInstance(int position) {
        TagChooseFragment fragment = new TagChooseFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_choose_fragment, container, false);
        myGridView = (MyGridView)view.findViewById(R.id.gridview);

        fragmentPosition = getArguments().getInt("position");

        if (fragmentPosition >= BillWizFragmentManager.tagChooseFragments.size()) {
            while (fragmentPosition >= BillWizFragmentManager.tagChooseFragments.size()) {
                BillWizFragmentManager.tagChooseFragments.add(new TagChooseFragment());
            }
        }
        BillWizFragmentManager.tagChooseFragments.set(fragmentPosition, this);

        tagAdapter = new TagChooseGridViewAdapter(getActivity(), fragmentPosition);

        myGridView.setAdapter(tagAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((OnTagItemSelectedListener)activity).onTagItemPicked(position);
                    ((OnTagItemSelectedListener)activity).onAnimationStart(RecordManager.TAGS.get(fragmentPosition * 8 + position + 2).getId());
                } catch (ClassCastException cce){
                    cce.printStackTrace();
                }
            }
        });
        return view;
    }

    public interface OnTagItemSelectedListener {
        void onTagItemPicked(int position);
        void onAnimationStart(int id);
    }

    public void updateTags() {
        ((BaseAdapter)myGridView.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)myGridView.getAdapter()).notifyDataSetInvalidated();
        myGridView.invalidateViews();
    }

}
