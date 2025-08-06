package com.phresh.presenter;

import com.phresh.view.IView;

public interface IPresenter<V extends IView<?>> {

    V getView();

    void setView(V view);
}
