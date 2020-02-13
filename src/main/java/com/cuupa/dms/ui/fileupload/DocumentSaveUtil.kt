package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.Constants
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.util.stream.Collectors

class DocumentSaveUtil(private val properties: FileUploadProperties, private val storageService: StorageService, private val username: String) {

    fun save() {
        val path = writeFile()
        path?.let { writeDatabase(it) }
    }

    private fun writeDatabase(path: Path) {
        storageService.save(Document(path.toString(),
                properties.getFilename().trim { it <= ' ' },
                properties.getFrom().trim { it <= ' ' },
                username,
                LocalDateTime.of(properties.getDate(), properties.getTime()),
                properties.getTags().map { name: String -> Tag(name) }))
    }

    private fun writeFile(): Path? {
        val path = Paths.get(Constants.DOCUMENTFOLDER +
                File.separator +
                username +
                File.separator +
                properties.getFilename())
        if (!path.toFile().exists()) {
            path.toFile().mkdirs()
        }
        val finalPath = findFinalFilepath(path)
        Files.write(finalPath, properties.content, StandardOpenOption.CREATE_NEW)
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
        return File(path.toFile().absolutePath, maxFoundFilename.inc().toString() + ".pdf").toPath()
    }
}