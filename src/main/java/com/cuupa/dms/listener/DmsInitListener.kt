package com.cuupa.dms.listener

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.ui.login.LoginScreen
import com.cuupa.dms.ui.register.RegisterView
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Component

@EnableAutoConfiguration
@Component
class DmsInitListener : VaadinServiceInitListener {
    @Autowired
    private val accessControl: AccessControl? = null

    override fun serviceInit(serviceInitEvent: ServiceInitEvent) {
        serviceInitEvent.source
                .addUIInitListener { uiInitEvent: UIInitEvent ->
                    uiInitEvent.ui.addBeforeEnterListener { enterEvent: BeforeEnterEvent ->
                        if (RegisterView::class.java == enterEvent.navigationTarget) {
                            return@addBeforeEnterListener
                        }
                        if (!accessControl!!.isUserSingedIn) {
                            if (LoginScreen::class.java != enterEvent.navigationTarget) {
                                enterEvent.rerouteTo(LoginScreen::class.java)
                            }
                        }
                    }
                }
    }
}