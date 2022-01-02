package com.uptosmth.chronos.jetbrains;

public class Heartbeat {
    private final long timestamp;
    private final String file;
    private final String project;

    public Heartbeat(long timestamp, String file, String project) {
        this.timestamp = timestamp;
        this.file = file;
        this.project = project;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFile() {
        return file;
    }

    public String getProject() {
        return project;
    }

    @Override
    public String toString() {
        return String.format(
                "Heartbeat{timestamp=%d, file='%s', project='%s'}", timestamp, file, project);
    }

    public String toJsonString() {
        return String.format(
                "{\"timestamp\":%d, \"file\":\"%s\", \"project\":\"%s\"}",
                timestamp, file, project);
    }
}
