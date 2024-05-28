package freeswitch.outbound;


import org.freeswitch.esl.client.transport.event.EslEvent;
import org.jboss.netty.channel.Channel;


public class EventProcessor {
    private final MultiThreadedEventProcessor processor;

    public EventProcessor() {
        this.processor = new MultiThreadedEventProcessor();
    }

    public void onEvent(Channel channel, EslEvent e) {
        System.out.println("Processing event: " + e);
        processor.processEvent(channel,e);
    }
}