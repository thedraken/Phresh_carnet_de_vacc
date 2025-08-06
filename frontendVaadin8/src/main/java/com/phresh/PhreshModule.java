package com.phresh;

import com.google.inject.AbstractModule;
import com.phresh.presenter.LoginPresenter;

public class PhreshModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LoginPresenter.class).toInstance(new LoginPresenter());
    }
}
