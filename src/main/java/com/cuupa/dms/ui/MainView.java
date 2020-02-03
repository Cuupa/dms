package com.cuupa.dms.ui;

import com.cuupa.dms.authentication.AccessControlFactory;
import com.cuupa.dms.ui.fileupload.FileUploadOverview;
import com.cuupa.dms.ui.overview.DocumentsOverview;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Theme(Lumo.class)
public class MainView extends AppLayout implements RouterLayout {

    public static final String TITLE = "Documents";

    public MainView() {

        createMenuToggle();

        Button logoutButton = createLogoutButton();
        Button uploadButton = createUploadButton();
        Button userButton = createUserButton();
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(uploadButton, userButton, logoutButton);
        buttonLayout.setFlexGrow(1, uploadButton, userButton, logoutButton);
        final HorizontalLayout top = createMenuHeader();
        Label title = createTitle();
        top.add(title, buttonLayout);
        top.setFlexGrow(1, title);
        top.setMinWidth("95%");

        addToNavbar(top);
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        //addClassName("centered-content");
        final VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.add(createMenuLink(Inbox.class, Inbox.VIEW_NAME, VaadinIcon.TIMER.create()));
        menuLayout.add(createMenuLink(DocumentsOverview.class,
                                      DocumentsOverview.VIEW_NAME,
                                      VaadinIcon.ARCHIVES.create()));
        addToDrawer(menuLayout);

        //add(textField, button);
    }

    private Button createUserButton() {
        Button userButton = new Button("Settings");
        userButton.setIcon(VaadinIcon.USER.create());
        return userButton;
    }

    private Button createUploadButton() {
        Button uploadButton = new Button("Upload");
        uploadButton.setIcon(VaadinIcon.UPLOAD.create());
        uploadButton.addClickListener(event -> uploadButton.getUI()
                                                           .ifPresent(ui -> ui.navigate(FileUploadOverview.VIEW_NAME)));
        return uploadButton;
    }

    private Button createLogoutButton() {
        Button
                logoutButton =
                new Button("Logout", e -> AccessControlFactory.getInstance().createAccessControl().singOut());
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());
        return logoutButton;
    }

    private Label createTitle() {
        final HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);
        //final Image image = new Image(resolvedImage, "");
        return new Label(TITLE);
    }

    private HorizontalLayout createMenuHeader() {
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        top.setClassName("menu-header");
        return top;
    }

    private RouterLink createMenuLink(final Class<? extends Component> viewClass, final String caption, final Icon icon) {
        final RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setClassName("menu-link");
        routerLink.add(icon);
        routerLink.add(new Span(caption));
        icon.setSize("24px");
        return routerLink;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void createMenuToggle() {
        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);
    }
}
