package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.Constants
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.ProcessParameters
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

class DocumentSaveService(private val storageService: StorageService, private val camundaService: CamundaService) {

    fun save(document: Document, content: ByteArray, dueDate: String?) {
        val path = writeFile(document.owner, document.filename, content)
        document.processInstanceId = camundaService.startProcess(ProcessParameters().withUser(document.owner).withDueDate(dueDate))
        path?.let { writeDatabase(it, document) }
    }

    private fun writeDatabase(path: Path, document: Document) {
        document.filename = path.toString()
        storageService.save(document)
    }

    private fun writeFile(owner: String, filename: String, content: ByteArray): Path? {
        val path = Paths.get(Constants.DOCUMENTFOLDER +
                File.separator +
                owner +
                File.separator +
                filename)
        if (!path.toFile().exists()) {
            path.toFile().mkdirs()
        }
        val finalPath = findFinalFilepath(path)
        Files.write(finalPath, content, StandardOpenOption.CREATE_NEW)
        return finalPath
    }

    private fun findFinalFilepath(path: Path): Path {
        val list = Files.list(path).collect(Collectors.toList())
        var maxFoundFilename = 0
        list.forEach { foundPath ->
            val foundFilename = foundPath.fileName.toString().split("\\p{Punct}".toRegex()).toTypedArray()[0]
            val currentFilename = foundFilename.toInt()
            if (currentFilename > maxFoundFilename) {
                maxFoundFilename = currentFilename
            }
        }
        return File(path.toFile().absolutePath, maxFoundFilename.inc().toString() + "." + FilenameUtils.getExtension
        (path
                .toString()))
                .toPath()
    }
}