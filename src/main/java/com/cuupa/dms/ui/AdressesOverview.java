package com.cuupa.dms.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "adresses", layout = MainView.class)
public class AdressesOverview extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = " Adresses";

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
