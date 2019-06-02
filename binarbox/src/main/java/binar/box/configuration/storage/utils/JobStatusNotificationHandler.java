package binar.box.configuration.storage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobStatusNotificationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatusNotificationHandler.class);

    private String jobId;

    private JobStatusNotification jobStatusNotificationMessage;

    public JobStatusNotificationHandler(String jobId) {
        super();
        this.jobId = jobId;
    }

    public void handle(JobStatusNotification jobStatusNotification) {
        if (jobStatusNotification.getJobId().equals(jobId)&& jobStatusNotification.getState().equals(JobStatusNotification.JobState.COMPLETED)) {
            LOGGER.debug("Received a notification indicating that the encoding job " + jobId + " has finished with the state "
                    + jobStatusNotification.getState());

            // storing the notification message so that the waiting thread can check that the job completed successfully or with an error
            jobStatusNotificationMessage = jobStatusNotification;

            // notify the thread that waits for a notification of the result of the job
            synchronized (this) {
                this.notify();
            }
        }
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobStatusNotification getJobStatusNotificationMessage() {
        return jobStatusNotificationMessage;
    }

    public void setJobStatusNotificationMessage(JobStatusNotification jobStatusNotificationMessage) {
        this.jobStatusNotificationMessage = jobStatusNotificationMessage;
    }
}
