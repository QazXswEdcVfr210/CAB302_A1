package com.qut.cab302_a1;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class PubSubTest {

    public static void main(String[] args) {
        // Replace with your project ID and Pub/Sub topic ID
        String projectId = "your-project-id";
        String topicId = "your-topic-id";

        publishMessage(projectId, topicId, "Hello, Google Cloud Functions!");
    }

    public static void publishMessage(String projectId, String topicId, String message) {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;
        try {
            // Create a publisher instance with the topic
            publisher = Publisher.newBuilder(topicName).build();

            // Convert message to bytes
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Publish the message and get a message ID future
            publisher.publish(pubsubMessage).get();

            System.out.println("Message published to topic: " + topicId);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error publishing message: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (publisher != null) {
                try {
                    // Shutdown the publisher
                    publisher.shutdown();
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
