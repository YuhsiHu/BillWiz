package com.billwiz.admin.billwiz.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.billwiz.admin.billwiz.model.SettingManager;
import com.billwiz.admin.billwiz.model.User;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.billwiz.admin.billwiz.BuildConfig;
import com.billwiz.admin.billwiz.model.Logo;
import com.billwiz.admin.billwiz.ui.CustomSliderView;
import com.billwiz.admin.billwiz.adapter.DrawerMonthViewRecyclerViewAdapter;
import com.billwiz.admin.billwiz.adapter.DrawerMonthViewRecyclerViewAdapter.OnItemClickListener;
import com.billwiz.admin.billwiz.adapter.MonthViewFragmentAdapter;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;
import com.billwiz.admin.billwiz.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 胡钰玺 on 2018/9/9
 * 月视图
 *
 * Updated by 胡钰玺 on 2018/9/11
 * bug：运行后跳转至输入界面，待修复
 *
 * Updated by 胡钰玺 on 2018/9/12
 * 修复了跳转错误的bug
 */

public class AccountBookMonthViewActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    private MonthViewFragmentAdapter monthModeAdapter = null;

    private Context mContext;

    private RecyclerView recyclerView;
    private DrawerMonthViewRecyclerViewAdapter drawerMonthViewRecyclerViewAdapter;

    private TextView userName;
    private TextView userEmail;

    private CircleImageView profileImage;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_account_book_month_view);

        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userName.setTypeface(BillWizUtil.typefaceLatoRegular);
        userEmail.setTypeface(BillWizUtil.typefaceLatoLight);

        //User 信息数据库下载未完成

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        View view = mViewPager.getRootView();
        TextView title = (TextView) view.findViewById(R.id.logo_white);
        title.setTypeface(BillWizUtil.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(BillWizUtil.GetTypeface(), Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

        monthModeAdapter = new MonthViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(monthModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(monthModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        if (monthModeAdapter.getCount() == 1) {
            mViewPager.getPagerTitleStrip().setVisibility(View.INVISIBLE);
        }

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorAndDrawable(
                        BillWizUtil.GetTagColor(RecordManager.TAGS.get(page).getId()),
                        BillWizUtil.GetTagDrawable(-3)
                );
            }
        });

        recyclerView = (RecyclerView) mDrawer.findViewById(R.id.recycler_view);
        drawerMonthViewRecyclerViewAdapter = new DrawerMonthViewRecyclerViewAdapter(mContext);
        recyclerView.setAdapter(drawerMonthViewRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        drawerMonthViewRecyclerViewAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mViewPager.getViewPager().setCurrentItem(position);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawers();
                    }
                }, 700);
            }
        });


        profileImage = (CircleImageView)mDrawer.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getLoggenOn()) {
                    BillWizUtil.showToast(mContext, R.string.change_logo_tip);
                } else {
                    BillWizUtil.showToast(mContext, R.string.login_tip);
                }
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String, Integer> urls = BillWizUtil.GetDrawerTopUrl();

        for(String name : urls.keySet()){
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(urls.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(customSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        loadLogo();
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {

        if (mDemoSlider != null) mDemoSlider.startAutoCycle();

        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MaterialViewPagerHelper.unregister(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    //下载用户头像部分没有改造完成，取决于数据库的使用
    private void loadLogo() {
        User user = BmobUser.getCurrentUser(BillWizApplication.getAppContext(), User.class);
        if (user != null) {
            try {
                File logoFile = new File(BillWizApplication.getAppContext().getFilesDir() + BillWizUtil.LOGO_NAME);
                if (!logoFile.exists()) {
                    // the local logo file is missed
                    // try to get from the server
                    BmobQuery<Logo> bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
                    bmobQuery.findObjects(BillWizApplication.getAppContext()
                            , new FindListener<Logo>() {
                                @Override
                                public void onSuccess(List<Logo> object) {
                                    // there has been an old logo in the server/////////////////////////////////////////////////////////
                                    String url = object.get(0).getFile().getFileUrl(BillWizApplication.getAppContext());
                                    if (BuildConfig.DEBUG) Log.d("BillWiz", "Logo in server: " + url);
                                    Ion.with(BillWizApplication.getAppContext()).load(url)
                                            .write(new File(BillWizApplication.getAppContext().getFilesDir()
                                                    + BillWizUtil.LOGO_NAME))
                                            .setCallback(new FutureCallback<File>() {
                                                @Override
                                                public void onCompleted(Exception e, File file) {
                                                    profileImage.setImageBitmap(BitmapFactory.decodeFile(
                                                            BillWizApplication.getAppContext().getFilesDir()
                                                                    + BillWizUtil.LOGO_NAME));
                                                }
                                            });
                                }
                                @Override
                                public void onError(int code, String msg) {
                                    // the picture is lost
                                    if (BuildConfig.DEBUG) Log.d("CoCoin", "Can't find the old logo in server.");
                                }
                            });
                } else {
                    // the user logo is in the storage
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(logoFile));
                    profileImage.setImageBitmap(b);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // use the default logo
            profileImage.setImageResource(R.drawable.default_user_logo);
        }
    }

}
