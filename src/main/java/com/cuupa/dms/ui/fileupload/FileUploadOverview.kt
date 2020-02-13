package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.ui.MainView
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired

@Route(value = "upload", layout = MainView::class)
class FileUploadOverview(@Autowired externSemanticService: ExternSemanticService, @Autowired storageService: StorageService, @Autowired accessControl: AccessControl) : HorizontalLayout(), HasUrlParameter<String?> {

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) { //viewLogic.enter(parameter);
    }

    companion object {
        const val VIEW_NAME = "upload"
    }

    init {
        val previewAndPropertiesLayout = PreviewAndPropertiesLayout(storageService, accessControl)
        val buffer = MultiFileMemoryBuffer()
        val upload = Upload(buffer)
        upload.addSucceededListener(FileUploadSucceededListener(externSemanticService,
                buffer,
                previewAndPropertiesLayout.properties,
                previewAndPropertiesLayout.preview))
        upload.addAllFinishedListener { previewAndPropertiesLayout.allFinishedEvent() }
        upload.minWidth = UIConstants.maxSize
        val layout = VerticalLayout(upload, previewAndPropertiesLayout)
        setSizeFull()
        add(layout)
    }
}