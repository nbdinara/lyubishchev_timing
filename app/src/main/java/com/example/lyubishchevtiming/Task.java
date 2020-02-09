package com.example.lyubishchevtiming;

public class Task {

    private String name;
    private String color;

    public Task (){
        // empty constructor
    }

    public Task (String name, String color){
        this.name = name;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public void setName (String name){
        this.name = name;
    }

    public void setColor (String color){
        this.color = color;
    }


}
