package binar.box.configuration.storage.utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class SqsQueueNotificationWorker implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqsQueueNotificationWorker.class);

	@Autowired
	private AmazonSQS amazonSQSClient;

	@Value("${aws.encoding.notification.queue.url}")
	private String awsEncodingNotificationsQueueUrl;

//	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Notification handlers that are informed every time a message arrives in the AWS SQS
	 */
	private final List<JobStatusNotificationHandler> notificationHandlers = new ArrayList<>();

	public void addHandler(JobStatusNotificationHandler notificationHandler) {
		synchronized (this.notificationHandlers) {
			this.notificationHandlers.add(notificationHandler);
		}
	}

	public void removeHandler(JobStatusNotificationHandler notificationHandler) {
		synchronized (this.notificationHandlers) {
			this.notificationHandlers.remove(notificationHandler);
		}
	}

	@Override
	public void run() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(awsEncodingNotificationsQueueUrl)
				.withVisibilityTimeout(0).withWaitTimeSeconds(20);
		while (true) {
			List<Message> messages = amazonSQSClient.receiveMessage(receiveMessageRequest).getMessages();
			if (messages != null) {
				synchronized (this.notificationHandlers) {
					for (Message message : messages) {
						JobStatusNotification jobStatusNotification;
						// reading the encoding job status from the Amazon SQS message
						try {
							@SuppressWarnings("unchecked")
							HashMap<String, String> jsonMessageFields = mapper.readValue(message.getBody(), HashMap.class);
							// parse the value of the "Message" field of the JSON message
							jobStatusNotification = mapper.readValue(jsonMessageFields.get("Message"), JobStatusNotification.class);
						} catch (IOException e) {
							LOGGER.error("An exception ocurred while trying to read an Amazon SQS message: ", e);
							continue;
						}

						// informing all notification handlers that a new AWS SQS message has arrived
						informHandlers(jobStatusNotification);

						// deleting the message from the queue
						amazonSQSClient.deleteMessage(new DeleteMessageRequest().withQueueUrl(awsEncodingNotificationsQueueUrl).withReceiptHandle(
								message.getReceiptHandle()));
					}
				}

			}
		}

	}

	/**
	 * Informs all notification handlers that a message containing the result of an encoding job has arrived in the AWS SQS
	 *
	 * @param jobStatusNotification
	 *            the result information of the encoding job
	 */
	private void informHandlers(JobStatusNotification jobStatusNotification) {
		for (JobStatusNotificationHandler notificationHandler : notificationHandlers) {
			notificationHandler.handle(jobStatusNotification);
		}
	}
}
