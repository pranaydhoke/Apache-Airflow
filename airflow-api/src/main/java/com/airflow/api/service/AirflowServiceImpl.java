package com.airflow.api.service;

import com.airflow.api.client.AirFlowClient;
import com.airflow.api.model.AirflowRequest;
import com.airflow.api.model.AirflowStatus;
import com.airflow.api.model.DagStatus;
import com.airflow.api.util.JsonUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AirflowServiceImpl implements AirflowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirflowServiceImpl.class);

    private final AirFlowClient airflowClient;
    private final RestTemplate restTemplate;
    private final JsonUtility jsonUtility;

    @Autowired
    public AirflowServiceImpl(AirFlowClient airflowClient, RestTemplate restTemplate,
                              JsonUtility jsonUtility) {
        this.airflowClient = airflowClient;
        this.restTemplate = restTemplate;
        this.jsonUtility = jsonUtility;
    }

    @Override
    public AirflowStatus createJob(AirflowRequest request) {
        LOGGER.debug("Triggering dag with dagId {}!", request.getDagId());

        ResponseEntity<String> responseEntity = airflowClient.triggerDag(request);  // Airflow client api call

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            AirflowStatus responseStatus = new AirflowStatus(request.getDagId());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);

            responseStatus.setExecutionDate(jsonObject.get("execution_date").toString());
            responseStatus.setStatus(jsonObject.get("message").toString());
            responseStatus.setRunId(jsonObject.get("run_id").toString());
            return responseStatus;
        }
        return null;
    }

    @Override
    public AirflowStatus getJobStatus(String dagId) {
        LOGGER.debug("fetching status of job within dagId {}!", dagId);
        AirflowStatus status = new AirflowStatus();

        String clientResponse = airflowClient.getDagStatus(dagId);
        if (clientResponse == null) {
            return new AirflowStatus(dagId);
        } else {
            List<DagStatus> dagStatuses = jsonUtility.convertCollectionFromJson(clientResponse, DagStatus.class);

            if (!dagStatuses.isEmpty()) {
                // Updating the status DTO using latest dag result object
                DagStatus dagStatus = dagStatuses.get(dagStatuses.size() - 1);
                status.setTotalRuns(String.valueOf(dagStatuses.size()));
                status.setCounter(String.valueOf(dagStatus.getId()));
                status.setDagId(dagStatus.getDagId());
                status.setRunId(dagStatus.getRunId());
                status.setExecutionDate(dagStatus.getExecutionDate());
                status.setStatus(dagStatus.getState());
                return status;
            } else {
                AirflowStatus noStatus = new AirflowStatus(dagId);
                noStatus.setStatus("NO STATUS AVAILABLE!");
                return noStatus;
            }
        }
    }

    @Override
    public AirflowStatus pauseJob(String dagId, String paused) {
        LOGGER.debug("updating the pause status of dag with dagId {}!", dagId);
        AirflowStatus status = new AirflowStatus(dagId);

        String response = airflowClient.pauseDag(dagId, paused);    // AirflowClient api call
        if (response != null) {
            status.setStatus("UPDATED");
        }
        return status;
    }

    @Override
    public AirflowStatus deleteJob(String dagId) {
        LOGGER.debug("deleting dag with dagId {}!", dagId);
        AirflowStatus status = new AirflowStatus(dagId);

        Integer statusCode = airflowClient.deleteJob(dagId);    // AirflowClient api call
        if (statusCode == 0) {
            status.setStatus("DELETED");
        } else {
            status.setStatus("ERROR");
        }
        return status;
    }

    @Override
    public AirflowStatus getTaskInfo(String dagId, String taskId) {
        LOGGER.debug("fetching the task: {} info within the dagId: {}!", taskId, dagId);
        AirflowStatus status = new AirflowStatus(dagId);

        String response = airflowClient.taskInfo(dagId, taskId);    // AirflowClient api call
        if (response != null) {
            status.setMessage(response);
            status.setStatus("SUCCESS");
            return status;
        }
        return status;
    }
}
