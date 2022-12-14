package com.hds.core.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseImpl {
    private CompositeDisposable mCompositeDisposable;

    protected P mPresenter;

    public BaseActivity mActivity;

    private ViewGroup container;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        beforeView();
        View view = layoutRes();
        setStatusBar();
        initView();
        initData();
        initEvent();
        return view;
    }

    protected void beforeView() {}

    protected abstract View layoutRes();

    protected abstract P createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected void setStatusBar(){
        ImmersionBar.with(this).init();
    }

    protected ViewGroup getContainer(){
        return container;
    }

    protected LayoutInflater getInflater(){
        return inflater;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (BaseActivity) context;
        super.onAttach(context);
    }


    @Override
    public boolean addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }
}
