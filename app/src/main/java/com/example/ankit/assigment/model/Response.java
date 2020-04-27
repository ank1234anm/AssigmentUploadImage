package com.example.ankit.assigment.model;

public class Response<T>
{
    T results;
    int resultCount ;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
    private String serverResponseTime;
    private boolean isSuccess;

    public String getServerResponseTime() {
        return serverResponseTime;
    }

    public void setServerResponseTime(String serverResponseTime) {
        this.serverResponseTime = serverResponseTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}
