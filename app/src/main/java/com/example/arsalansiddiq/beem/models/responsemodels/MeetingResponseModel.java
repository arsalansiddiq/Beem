package com.example.arsalansiddiq.beem.models.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jellani on 9/22/2018.
 */

public class MeetingResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
