package freeswitch.outbound;

import freeswitch.inbound.ConcurrentUser;
import io.netty.handler.codec.http.HttpRequest;
import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.web.client.RestTemplate;


import java.net.http.HttpClient;
import java.util.HashMap;



public class SampleOutboundHandler extends AbstractOutboundClientHandler {
    public static HashMap<String, ConcurrentUser> userMap = new HashMap<>();
    freeswitch.outbound.EventProcessor eventProcessor = new freeswitch.outbound.EventProcessor();





    @Override
    protected void handleConnectResponse(ChannelHandlerContext ctx, EslEvent event) {
        HashMap<String,Integer>users = new HashMap<String,Integer>();
        users.put("1004",100);
        users.put("1002",50);
        users.put("1001",50);
        users.put("1003",50);

        if (event.getEventName().equalsIgnoreCase("CHANNEL_DATA")) {

            userMap.put(event.getEventHeaders().get("Caller-Username"),new ConcurrentUser(event));

            Channel channel = ctx.getChannel();
            if(users.get("1004")>0 || users.get("1001")>0){
                eventProcessor.onEvent(channel,event);
            }

        } else {
            throw new IllegalStateException("Unexpected event after connect: [" + event.getEventName() + ']');
        }
    }


    @Override
    protected void handleEslEvent(ChannelHandlerContext ctx, EslEvent event) {
        System.out.println("received event:" + event);
    }

    @Override
    protected void handleDisconnectionNotice() {
        super.handleDisconnectionNotice();
        System.out.println("Received disconnection notice");
    }

}
