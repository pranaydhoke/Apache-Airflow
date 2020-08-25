package com.airflow.api.controller;

import com.airflow.api.model.AirflowRequest;
import com.airflow.api.model.AirflowStatus;
import com.airflow.api.service.AirflowService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path = "/v1")
public class AirflowAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirflowAPIController.class);

    private final AirflowService airflowService;
    private final Gson gson;

    public AirflowAPIController(AirflowService airflowService, Gson gson) {
        this.airflowService = airflowService;
        this.gson = gson;
    }

    @ApiOperation(value = "Triggers the Apache Airflow DAG via dagId", response = AirflowStatus.class)
    @PostMapping(value = "/job/trigger", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createJob(@RequestBody AirflowRequest request) {
        String message = null;
        try {
            AirflowStatus jobStatus = airflowService.createJob(request);
            if (Objects.nonNull(jobStatus)) {
                LOGGER.info("Successfully created job " + request.getDagId());
                return ResponseEntity.ok(jobStatus);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        message = "Failed to trigger dagId - " + request.getDagId() + " please investigate or DAG may be refreshing!";
        AirflowStatus status = new AirflowStatus(request.getDagId());
        status.setMessage(message);
        return new ResponseEntity<>(gson.toJson(status), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Provides the status of specific DAG via dagId", response = AirflowStatus.class)
    @GetMapping(value = "/job/status", produces = "application/json")
    public ResponseEntity<?> getJobStatus(@RequestParam(value = "jobId") String jobId) {
        String message = null;
        AirflowStatus jobStatus = airflowService.getJobStatus(jobId);

        if (jobStatus.getStatus().equals("ERROR")) {
            message = "No status available for dagId: " + jobId + " please investigate!";
            jobStatus.setMessage(message);
            return new ResponseEntity<>(gson.toJson(jobStatus), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jobStatus);
    }

    @ApiOperation(value = "Provides the status of task within the DAG via dagId & taskId", response = AirflowStatus.class)
    @GetMapping(value = "/task/status", produces = "application/json")
    public ResponseEntity<?> getTaskStatus(@RequestParam(value = "jobId") String jobId,
                                           @RequestParam(value = "taskId") String taskId) {
        String message = null;
        AirflowStatus jobStatus = airflowService.getTaskInfo(jobId, taskId);

        if (jobStatus.getStatus().equals("ERROR")) {
            message = "No status available for taskId: " + taskId + " within dagId: " + jobId +
                    " please investigate!";
            jobStatus.setMessage(message);
            return new ResponseEntity<>(gson.toJson(jobStatus), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jobStatus.getMessage());
    }

    @ApiOperation(value = "Deletes the specific DAG & Run history via dagId. Doesn't removes the DAG!", response = AirflowStatus.class)
    @DeleteMapping(value = "/job/delete", produces = "application/json")
    public ResponseEntity<?> deleteJob(@RequestParam(value = "jobId") String jobId) {
        String message;
        AirflowStatus jobStatus = airflowService.deleteJob(jobId);

        if (jobStatus.getStatus().equals("ERROR")) {
            message = "Failed to delete dagId: " + jobId + " please investigate or DAG may be refreshing!";
            jobStatus.setMessage(message);
            return new ResponseEntity<>(gson.toJson(jobStatus), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(jobStatus);
    }

    @ApiOperation(value = "Pause specific DAG via dagId & pause value", response = AirflowStatus.class)
    @PutMapping(value = "/job/paused", produces = "application/json")
    public ResponseEntity<?> pause(@RequestParam(value = "jobId") String jobId,
                                   @RequestParam(value = "pause") String paused) {
        String message;
        AirflowStatus jobStatus = airflowService.pauseJob(jobId, paused);

        if (jobStatus.getStatus().equals("ERROR")) {
            message = "Failed to update pause status of dagId: " + jobId + " with pause value: "
                    + paused + ", please investigate!";
            jobStatus.setMessage(message);
            return new ResponseEntity<>(gson.toJson(jobStatus), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(jobStatus);
    }
}
