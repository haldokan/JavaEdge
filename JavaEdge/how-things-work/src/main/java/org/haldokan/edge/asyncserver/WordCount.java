package org.haldokan.edge.asyncserver;

import java.util.Map;
import java.util.concurrent.Future;

public class WordCount implements ServiceRequest {
    private final String fileName;
    private final long threadId;
    private Future<Map<String, Long>> wordcount;

    public WordCount(String fileName, long threadId) {
	this.fileName = fileName;
	this.threadId = threadId;
    }

    /**
     * @return the fileName
     */
    public final String getFileName() {
	return fileName;
    }

    /**
     * @return the threadId
     */
    public final long getThreadId() {
	return threadId;
    }

    public Future<Map<String, Long>> getWordcount() {
	return wordcount;
    }

    public void setWordcount(Future<Map<String, Long>> wordcount) {
	this.wordcount = wordcount;
    }

    @Override
    public ServiceName getServiceName() {
	return ServiceName.WORD_COUNT;
    }

    @Override
    public String toString() {
	return "WordCount [fileName=" + fileName + ", threadId=" + threadId + ", wordcount=" + wordcount + "]";
    }
}