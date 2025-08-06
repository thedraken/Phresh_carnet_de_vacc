package com.phresh.view;

import com.phresh.presenter.IPresenter;
import com.vaadin.navigator.View;

public interface IView<P extends IPresenter<?>> extends View {

    P getPresenter();

}
