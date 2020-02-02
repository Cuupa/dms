package com.cuupa.dms.service.extern;

import java.util.List;

public class SemanticResult {

    private final String topicName;

    private String sender;

    private List<Metadata> metaData;

    public SemanticResult(String topicName, List<Metadata> metaData) {
        this.topicName = topicName;
        this.metaData = metaData;
    }

    public SemanticResult(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public List<Metadata> getMetaData() {
        return metaData;
    }

    public String getSender() {
        return sender;
    }

    void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Topic: " + topicName + " Metadata: " + metaData;
    }
}
