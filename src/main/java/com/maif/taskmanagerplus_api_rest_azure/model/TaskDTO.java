package com.maif.taskmanagerplus_api_rest_azure.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDTO {
    private String title;
    private String description;
    private String dueDate;
    private Boolean completed;

    // Getters and Setters, I am using lombok for this
    
}
