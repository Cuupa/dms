package com.cuupa.dms.listener;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.authentication.AccessControlFactory;
import com.cuupa.dms.ui.login.LoginScreen;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class DmsInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

        serviceInitEvent.getSource()
                        .addUIInitListener(uiInitEvent -> uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                            if (!accessControl.isUserSingedIn() &&
                                !LoginScreen.class.equals(enterEvent.getNavigationTarget())) {
                                enterEvent.rerouteTo(LoginScreen.class);
                            }
                        }));
    }
}
