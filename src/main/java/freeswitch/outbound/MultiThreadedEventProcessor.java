package freeswitch.outbound;

import org.freeswitch.esl.client.transport.event.EslEvent;

import org.jboss.netty.channel.Channel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedEventProcessor {
    private final ExecutorService executorService;
    private static int id =0;

    public MultiThreadedEventProcessor() {
        // Get the number of available processors
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        // Create a thread pool with the number of available processors
        this.executorService = Executors.newFixedThreadPool(numberOfProcessors);
    }

    public void processEvent(Channel channel, EslEvent e) {
        // Submit the event processing task to the thread pool
        executorService.submit(() -> handleEvent(channel, e));
    }

    private void handleEvent(Channel channel, EslEvent e) {
        // Process the event here
        Thread t = new Thread( new FsMessageSenderThread(channel,e) );
        t.setName("Caller Thread");
        t.start();
        // Add actual event processing logic here
    }

    public void shutdown() {
        // Gracefully shut down the executor service
        executorService.shutdown();
    }
}
