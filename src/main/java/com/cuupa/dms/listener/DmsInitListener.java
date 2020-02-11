package com.cuupa.dms.listener;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.ui.login.LoginScreen;
import com.cuupa.dms.ui.register.RegisterScreen;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@EnableAutoConfiguration
@Component
public class DmsInitListener implements VaadinServiceInitListener {

    @Autowired
    private AccessControl accessControl;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {

        serviceInitEvent.getSource()
                        .addUIInitListener(uiInitEvent -> uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                            if (RegisterScreen.class.equals(enterEvent.getNavigationTarget())) {
                                return;
                            }

                            if (!accessControl.isUserSingedIn()) {
                                if (!LoginScreen.class.equals(enterEvent.getNavigationTarget())) {
                                    enterEvent.rerouteTo(LoginScreen.class);
                                }
                            }
                        }));
    }
}
