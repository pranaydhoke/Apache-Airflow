package com.airflow.api.client;

import com.airflow.api.model.AirflowRequest;
import com.airflow.api.util.PropertyLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AirFlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirFlowClient.class);
    private static final String DELIMITER = "/";
    private static final String STATUS_ENDPOINT = "api/experimental/dags";
    private static final String DAG_RUNS = "dag_runs";

    private Map<String, String> clientProperties;
    private RestTemplate restTemplate;


    public AirFlowClient(Map<String, String> properties, RestTemplate restTemplate) {
        this.clientProperties = properties;
        this.restTemplate = restTemplate;
    }

    public String getDagStatus(String dagId) {
        String endpointURL = null;
        String response = null;
        try {
            endpointURL = buildEndpointUrl(dagId, null);
            response = restTemplate.getForObject(endpointURL, String.class);
            LOGGER.info("Successfully fetch dag status for dagId: {}", dagId);
        } catch (Exception ex) {
            LOGGER.error("Failed to fetch dag status, dagId: {}", dagId, ex);
        }
        return response;
    }

    public ResponseEntity<String> triggerDag(AirflowRequest request) {
        String endpointURL = null;
        ResponseEntity<String> response = null;
        try {
            HttpHeaders headers = getHeaders(request);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request.getConfig(), headers);

            endpointURL = buildEndpointUrl(request.getDagId(), null);
            response = restTemplate.postForEntity(endpointURL, entity, String.class);

            LOGGER.info("Successfully triggered dagId: {}", request.getDagId());
        } catch (Exception ex) {
            LOGGER.error("Failed to execute, dagId: {}", request.getDagId(), ex);
        }
        return response;
    }

    public String pauseDag(String dagId, String paused) {
        String endpointURL = null;
        String response = null;
        try {
            endpointURL = buildEndpointUrl(dagId, paused);
            response = restTemplate.getForObject(endpointURL, String.class);

            LOGGER.info("Successfully updated the pause status of  dagId: {}", dagId);
        } catch (Exception ex) {
            LOGGER.error("Failed to update pause status of dagId: {}", dagId, ex);
        }
        return response;
    }

    public String taskInfo(String dagId, String taskId) {
        String endpointURL;
        String response = null;
        try {
            endpointURL = buildEndpointUrl(dagId, taskId);
            response = restTemplate.getForObject(endpointURL, String.class);

            LOGGER.info("Successfully updated the pause status of  dagId: {}", dagId);
        } catch (Exception ex) {
            LOGGER.error("Failed to update pause status of dagId: {}", dagId, ex);
        }
        return response;
    }

    public Integer deleteJob(String dagId) {
        Integer statusCode = 1;
        PropertyLoader instance = PropertyLoader.getInstance();
        Map<String, String> airflowCommands = instance.getAirflowCommands("/airflow-commands.properties");

        try {
            String deleteCommand = airflowCommands.get("delete.airflow.job");
            LOGGER.info("Cli prepared...." + deleteCommand);
            Process exec = Runtime.getRuntime().exec(String.format(deleteCommand, dagId));
            AirflowProcessor processor = new AirflowProcessor(exec.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(processor);
            statusCode = exec.waitFor();
            LOGGER.info("Delete process exited with status code {} ", statusCode);
        } catch (InterruptedException | IOException ex) {
            LOGGER.error("Failed to execute delete command!", ex);
        }
        return statusCode;
    }

    private HttpHeaders getHeaders(AirflowRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.noCache());

        return headers;
    }

    private String buildEndpointUrl(String dagId, String subTasks) {
        StringBuilder builder = new StringBuilder();
        builder.append(clientProperties.get("ENDPOINT_URL"))
                .append(DELIMITER)
                .append(STATUS_ENDPOINT).append(DELIMITER)
                .append(dagId).append(DELIMITER);
        if (subTasks != null) {
            if (subTasks.equals("true") || subTasks.equals("false")) {
                builder.append("paused").append(DELIMITER).append(subTasks);
            } else {
                builder.append("tasks").append(DELIMITER).append(subTasks);
            }
        } else {
            builder.append(DAG_RUNS);
        }
        return builder.toString();
    }
}


class AirflowProcessor implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public AirflowProcessor(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
    }
}