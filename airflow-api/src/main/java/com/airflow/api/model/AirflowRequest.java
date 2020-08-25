package com.airflow.api.model;

import java.util.Map;

public class AirflowRequest {

    private String dagId;
    private Map<String, Object> config;

    public AirflowRequest() {
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "AirflowRequest{" +
                "dagId='" + dagId + '\'' +
                ", config=" + config +
                '}';
    }
}
