package com.airflow.api.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dag_id",
        "dag_run_url",
        "execution_date",
        "id",
        "run_id",
        "start_date",
        "state"
})
public class DagStatus {

    @JsonProperty("dag_id")
    private String dagId;
    @JsonProperty("dag_run_url")
    private String dagRunUrl;
    @JsonProperty("execution_date")
    private String executionDate;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("run_id")
    private String runId;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("state")
    private String state;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dag_id")
    public String getDagId() {
        return dagId;
    }

    @JsonProperty("dag_id")
    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    @JsonProperty("dag_run_url")
    public String getDagRunUrl() {
        return dagRunUrl;
    }

    @JsonProperty("dag_run_url")
    public void setDagRunUrl(String dagRunUrl) {
        this.dagRunUrl = dagRunUrl;
    }

    @JsonProperty("execution_date")
    public String getExecutionDate() {
        return executionDate;
    }

    @JsonProperty("execution_date")
    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("run_id")
    public String getRunId() {
        return runId;
    }

    @JsonProperty("run_id")
    public void setRunId(String runId) {
        this.runId = runId;
    }

    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("start_date")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}