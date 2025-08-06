package com.phresh.presenter;

import com.phresh.view.LoginView;

public class LoginPresenter implements IPresenter<LoginView> {

    private LoginView view;

    public LoginPresenter() {

    }

    @Override
    public LoginView getView() {
        return view;
    }

    @Override
    public void setView(LoginView view) {
        this.view = view;
    }

    public void doLogin(String username, String password) {

    }
}
