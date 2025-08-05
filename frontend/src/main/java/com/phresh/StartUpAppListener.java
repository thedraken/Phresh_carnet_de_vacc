package com.phresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartUpAppListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AppSetupService appSetupService;

    //TODO Use this to create the test users or not
    private final boolean createTestUsers = true;

    @Autowired
    public StartUpAppListener(AppSetupService appSetupService) {
        this.appSetupService = appSetupService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        appSetupService.startApp(createTestUsers);
    }
}
