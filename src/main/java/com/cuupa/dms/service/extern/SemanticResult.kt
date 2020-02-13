package com.cuupa.dms.service.extern

class SemanticResult {

    val topicName: String
    var sender: String? = null
    var metaData: List<Metadata> = mutableListOf()
        private set

    constructor(topicName: String, metaData: List<Metadata>) {
        this.topicName = topicName
        this.metaData = metaData
    }

    constructor(topicName: String) {
        this.topicName = topicName
    }

    override fun toString(): String {
        return "Topic: $topicName Metadata: $metaData"
    }
}