package com.example.y.mvp.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.y.mvp.ImageDetailInfo;
import com.example.y.mvp.R;
import com.example.y.mvp.adapter.ImageDetailAdapter;
import com.example.y.mvp.mvp.presenter.ImageDetailPresenterImpl;
import com.example.y.mvp.mvp.presenter.Presenter;
import com.example.y.mvp.mvp.presenter.ToolBarItemPresenterImpl;
import com.example.y.mvp.mvp.view.BaseView;
import com.example.y.mvp.network.Api;
import com.example.y.mvp.utils.ActivityUtils;
import com.example.y.mvp.utils.CompetenceUtils;
import com.example.y.mvp.utils.UIUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * by y on 2016/4/29.
 */
public class ImageDetailActivity extends DarkViewActivity
        implements BaseView.ImageDetailView, BaseView.ToolBarItemView
        , Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private Toolbar toolBar;
    private int pos;
    private LinkedList<ImageDetailInfo> list;
    private Presenter.ImageDetailPresenter imageDetailPresenter;
    private Presenter.ToolBarItemPresenter toolBarItemPresenter;
    private ImageDetailAdapter bigImageAdapter;


    public static void startIntent(int id, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("title", title);
        ActivityUtils.startActivity(ImageDetailActivity.class, bundle);
    }

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        setSupportActionBar(toolBar);
        CompetenceUtils.Storage();
        init();
    }

    @Override
    protected void initById() {
        viewPager = getView(R.id.viewPager);
        toolBar = getView(R.id.toolBar);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageDetailPresenter.competence(requestCode, grantResults);
    }


    private void init() {
        imageDetailPresenter = new ImageDetailPresenterImpl(this);
        toolBarItemPresenter = new ToolBarItemPresenterImpl(this);
        list = new LinkedList<>();
        imageDetailPresenter.requestNetWork(imageDetailPresenter.getBundle());
        bigImageAdapter = new ImageDetailAdapter(list);
        toolBar.setOnMenuItemClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void netWorkError() {
        ActivityUtils.Toast(UIUtils.getString(R.string.network_error));
    }


    @Override
    public void setData(List<ImageDetailInfo> datas) {
        if (!datas.isEmpty()) {
            list.addAll(datas);
            viewPager.setAdapter(bigImageAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void switchShare() {
        ActivityUtils.share(Api.IMAGER_URL + list.get(pos).getSrc());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        toolBarItemPresenter.switchId(item.getItemId());
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position != 0) {
            setSwipeBackEnable(false);
        } else {
            setSwipeBackEnable(true);
        }
        pos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
