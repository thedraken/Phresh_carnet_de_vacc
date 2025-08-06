package com.phresh.presenter;

import com.phresh.AppSetupService;
import com.phresh.StartUpAppListener;
import com.phresh.exceptions.RuleException;
import com.phresh.security.SecurityService;
import com.phresh.view.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

@Component
public class LoginPresenter implements IPhreshPresenter<LoginView> {

    private static final Logger logger = Logger.getLogger(LoginPresenter.class.getSimpleName());

    private final SecurityService securityService;
    private final AppSetupService appSetupService;
    private boolean initialSetupToBeRan = true;

    @Autowired
    public LoginPresenter(SecurityService securityService, AppSetupService appSetupService) {
        this.securityService = securityService;


        this.appSetupService = appSetupService;
    }

    public void login(String username, String password) throws RuleException, LoginException {
        securityService.login(username, password);
    }

    public void createTestUsers() {
        if (StartUpAppListener.createTestUsers && initialSetupToBeRan) {
            initialSetupToBeRan = false;
            //Funny hack to create users being done now, would have done via the startup listener,
            // but was having issues where DB was locked or was flushing entities which were not persisted...
            logger.warning("Creating test users...");
            appSetupService.createTestUsers();
        }
    }

}
