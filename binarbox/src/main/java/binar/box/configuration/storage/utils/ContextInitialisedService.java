package binar.box.configuration.storage.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextInitialisedService implements InitializingBean {

    @Autowired
    private SqsQueueNotificationWorker sqsQueueNotificationWorker;

    @Override
    public void afterPropertiesSet() throws Exception {
        // runs a separate thread that continuously pools for encoding job notifications in the AWS SQS queue

        Thread thread = new Thread(sqsQueueNotificationWorker);
        thread.start();
    }
}
