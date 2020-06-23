package com.example.velimatti.labyrinthleaderboard;

public class LabyrinthRun {
    public LabyrinthRun( Integer rank, Integer time, String name, String ascendancy ){
        this.rank = rank;
        this.time = time;
        this.name = name;
        this.ascendancy = ascendancy;
    }
    public Integer rank = -1;
    public Integer time = -1;
    public String name = "unknown";
    public String ascendancy = "unknown";
}
