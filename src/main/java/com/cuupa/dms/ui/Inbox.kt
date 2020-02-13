package com.cuupa.dms.ui

import com.cuupa.dms.ui.MainView
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route

@Route(value = "inbox", layout = MainView::class)
class Inbox : HorizontalLayout(), HasUrlParameter<String?> {
    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) { //viewLogic.enter(parameter);
    }

    companion object {
        const val VIEW_NAME = " Inbox"
    }
}