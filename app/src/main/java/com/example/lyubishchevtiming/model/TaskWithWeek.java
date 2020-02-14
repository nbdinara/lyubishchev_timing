package com.example.lyubishchevtiming.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskWithWeek extends Week{

    @Embedded public Week week;
    @Relation(parentColumn = "id", entityColumn = "week_id", entity =
            Task.class)
    public Task task;
}
