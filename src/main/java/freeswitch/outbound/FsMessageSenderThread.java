package freeswitch.outbound;

import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.jboss.netty.channel.Channel;

import static java.lang.Thread.sleep;

public class FsMessageSenderThread implements Runnable {
    CallOperator operator;
    Channel channel;
    EslEvent e;
    public FsMessageSenderThread(Channel channel, EslEvent e) {
        this.channel = channel;
        this.e = e;
        this.operator = new CallOperator();
    }
    @Override
    public void run() {
        String calledNumber = e.getEventHeaders().get("Channel-Destination-Number");
        System.out.println(Thread.currentThread().getName() +" " + calledNumber);
        operator.bridgeCall(channel,calledNumber);
    }
}
