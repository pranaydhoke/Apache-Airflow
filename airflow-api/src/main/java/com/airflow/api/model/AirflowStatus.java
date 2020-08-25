package com.airflow.api.model;

import java.time.Instant;

public class AirflowStatus {

    private String dagId;
    private String runId;
    private String counter;
    private String totalRuns;
    private String status;
    private String executionDate;
    private String message = "Everything is working fine!";

    public AirflowStatus() {
    }

    public AirflowStatus(String dagId) {
        this.setCounter("0");
        this.setTotalRuns("0");
        this.setRunId("N/A");
        this.setExecutionDate(Instant.now().toString());
        this.setStatus("ERROR");
        this.setDagId(dagId);
        this.setMessage("Everything is working fine!");
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(String totalRuns) {
        this.totalRuns = totalRuns;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AirflowStatus{" +
                "dagId='" + dagId + '\'' +
                ", runId='" + runId + '\'' +
                ", counter='" + counter + '\'' +
                ", totalRuns='" + totalRuns + '\'' +
                ", status='" + status + '\'' +
                ", executionDate='" + executionDate + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
