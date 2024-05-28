package freeswitch.inbound;

import freeswitch.outbound.SampleOutboundHandler;
import org.freeswitch.esl.client.transport.event.EslEvent;

public class ConcurrentUser {
    public final String callNumber;
    public final String userId;
    public final String freeswitchIp;
    public final String calledNumber;
    public final String channelStateNumber;
    public final String socketHost;
    public final String channelName;
    public final String eventName;

    public ConcurrentUser(EslEvent event ){
        this.callNumber = event.getEventHeaders().get("Caller-Username");
        this.userId = event.getEventHeaders().get("Unique-ID");
        this.freeswitchIp = event.getEventHeaders().get("FreeSWITCH-IPv4");
        this.calledNumber = event.getEventHeaders().get("Channel-Destination-Number");
        this.channelStateNumber = event.getEventHeaders().get("Channel-State-Number");
        this.socketHost = event.getEventHeaders().get("variable_socket_host");
        this.channelName = event.getEventHeaders().get("Channel-Name");
        this.eventName = event.getEventHeaders().get("Event-Name");
    }
}
