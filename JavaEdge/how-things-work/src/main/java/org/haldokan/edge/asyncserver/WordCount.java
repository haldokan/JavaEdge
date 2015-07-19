package org.haldokan.edge.asyncserver;

public class WordCount implements ServiceRequest {
    private String fileName;
    private long threadId;
    private long writeTime;
    private long readTime;
    private int count;
    
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
     * @param fileName the fileName to set
     */
    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the threadId
     */
    public final long getThreadId() {
        return threadId;
    }

    /**
     * @param threadId the threadId to set
     */
    public final void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    /**
     * @return the writeTime
     */
    public final long getWriteTime() {
        return writeTime;
    }

    /**
     * @param writeTime the writeTime to set
     */
    public final void setWriteTime(long writeTime) {
        this.writeTime = writeTime;
    }

    /**
     * @return the readTime
     */
    public final long getReadTime() {
        return readTime;
    }

    /**
     * @param readTime the readTime to set
     */
    public final void setReadTime(long readTime) {
        this.readTime = readTime;
    }
    
    public int getCount() {
	return count;
    }
    
    @Override
    public ServiceName getServiceName() {
	return ServiceName.WORD_COUNT;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "WordCount [fileName=" + fileName + ", threadId=" + threadId + ", writeTime=" + writeTime
		+ ", readTime=" + readTime + "]";
    }
}
