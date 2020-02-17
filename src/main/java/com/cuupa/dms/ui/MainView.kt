package com.cuupa.dms.ui

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.ui.fileupload.FileUploadOverview
import com.cuupa.dms.ui.inbox.Inbox
import com.cuupa.dms.ui.overview.DocumentsOverview
import com.cuupa.dms.ui.settings.UserSettings
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.beans.factory.annotation.Autowired

/**
 * A sample Vaadin view class.
 *
 *
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 *
 *
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Theme(Lumo::class)
class MainView(@Autowired accessControl: AccessControl) : AppLayout(), RouterLayout {

    private fun createUserButton(): Button {
        val userButton = Button(UIConstants.settings, VaadinIcon.USER.create())
        userButton.addClickListener { userButton.ui.ifPresent { ui: UI -> ui.navigate(UserSettings.VIEW_NAME) } }
        return userButton
    }

    private fun createUploadButton(): Button {
        val uploadButton = Button(UIConstants.upload, VaadinIcon.UPLOAD.create(), ComponentEventListener { })
        uploadButton.addClickListener {
            uploadButton.ui
                    .ifPresent { ui: UI -> ui.navigate(FileUploadOverview.VIEW_NAME) }
        }
        return uploadButton
    }

    private fun createLogoutButton(accessControl: AccessControl): Button {
        val logoutButton = Button("Logout", ComponentEventListener { accessControl.singOut() })
        logoutButton.icon = VaadinIcon.SIGN_OUT.create()
        return logoutButton
    }

    private fun createTitle(): Label {
        val menuLayout = HorizontalLayout()
        menuLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.START
        return Label(TITLE)
    }

    private fun createMenuHeader(): HorizontalLayout {
        val top = HorizontalLayout()
        top.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        top.className = "menu-header"
        return top
    }

    private fun createMenuLink(viewClass: Class<out Component?>, caption: String, icon: Icon): RouterLink {
        val routerLink = RouterLink(null, viewClass)
        routerLink.className = "menu-link"
        routerLink.add(icon)
        routerLink.add(Span(caption))
        icon.setSize("24px")
        return routerLink
    }

    private fun createMenuToggle() {
        val drawerToggle = DrawerToggle()
        drawerToggle.addClassName("menu-toggle")
        addToNavbar(drawerToggle)
    }

    companion object {
        const val TITLE = "Documents"
    }

    init {
        createMenuToggle()
        val logoutButton = createLogoutButton(accessControl)
        val uploadButton = createUploadButton()
        val userButton = createUserButton()
        val buttonLayout = HorizontalLayout()
        buttonLayout.add(uploadButton, userButton, logoutButton)
        buttonLayout.setFlexGrow(1.0, uploadButton, userButton, logoutButton)
        val top = createMenuHeader()
        val title = createTitle()
        top.add(title, buttonLayout)
        top.setFlexGrow(1.0, title)
        top.minWidth = "95%"
        addToNavbar(top)

        val menuLayout = VerticalLayout()
        menuLayout.add(createMenuLink(Inbox::class.java, Inbox.VIEW_NAME, VaadinIcon.INBOX.create()))
        menuLayout.add(createMenuLink(DocumentsOverview::class.java,
                DocumentsOverview.VIEW_NAME,
                VaadinIcon.MAILBOX.create()))
        menuLayout.add(createMenuLink(AdressesOverview::class.java,
                AdressesOverview.VIEW_NAME,
                VaadinIcon.USER_CARD.create()))
        addToDrawer(menuLayout)
    }
}