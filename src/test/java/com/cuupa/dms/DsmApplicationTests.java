package com.cuupa.dms;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.theme.lumo.Lumo;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

public class DsmApplicationTests extends AbstractViewTest {

    public void clickingButtonShowsNotification() {
        Assert.assertFalse($(NotificationElement.class).exists());

        $(ButtonElement.class).first().click();

        Assert.assertTrue($(NotificationElement.class).waitForFirst().isOpen());
    }

    public void clickingButtonTwiceShowsTwoNotifications() {
        Assert.assertFalse($(NotificationElement.class).exists());

        ButtonElement button = $(ButtonElement.class).first();
        button.click();
        button.click();

        Assert.assertEquals(2, $(NotificationElement.class).all().size());
    }

    public void buttonIsUsingLumoTheme() {
        WebElement element = $(ButtonElement.class).first();
        assertThemePresentOnElement(element, Lumo.class);
    }
}
