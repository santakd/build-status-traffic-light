package de.zalando.buildstatus.job;

public class JobAlreadyExistsException extends RuntimeException {
    public JobAlreadyExistsException(String job) {
        super("The job [" + job + "] already exists. Please choose another name");
    }
}