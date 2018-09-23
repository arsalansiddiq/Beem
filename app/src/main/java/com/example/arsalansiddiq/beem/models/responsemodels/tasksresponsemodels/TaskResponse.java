package com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jellani on 9/21/2018.
 */

public class TaskResponse {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("tasks")
        @Expose
        private List<Task> tasks = null;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<Task> getTasks() {
            return tasks;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

}
