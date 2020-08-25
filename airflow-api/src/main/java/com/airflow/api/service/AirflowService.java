package com.airflow.api.service;

import com.airflow.api.model.AirflowRequest;
import com.airflow.api.model.AirflowStatus;

import java.util.List;


public interface AirflowService {

    AirflowStatus createJob(AirflowRequest entity);

    AirflowStatus getJobStatus(String dagId);

    AirflowStatus deleteJob(String dagId);

    AirflowStatus getTaskInfo(String dagId, String taskId);

    AirflowStatus pauseJob(String jobId, String paused);
}
