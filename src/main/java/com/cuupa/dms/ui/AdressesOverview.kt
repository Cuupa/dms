package com.cuupa.dms.ui

import com.cuupa.dms.ui.MainView
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route

@Route(value = "adresses", layout = MainView::class)
class AdressesOverview : HorizontalLayout(), HasUrlParameter<String?> {
    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {}

    companion object {
        const val VIEW_NAME = " Adresses"
    }
}