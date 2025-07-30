package com.phresh.presenter;

import com.phresh.domain.User;
import com.phresh.security.PasswordEncryptor;
import com.phresh.view.CreateUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUserPresenter implements IPhreshPresenter<CreateUserView> {

    private final PasswordEncryptor passwordEncryptor;

    @Autowired
    public CreateUserPresenter(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public void createUser(User user) {
        user.setPassword(passwordEncryptor.encryptPassword(user.getPassword()));
    }
    
}
