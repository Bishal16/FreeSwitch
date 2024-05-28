package freeswitch.outbound;

import freeswitch.cnfig.FreeSwitchConfig;
import org.freeswitch.esl.client.outbound.SocketClient;

public class OutboundBuilder {

    public static void build(FreeSwitchConfig config) throws InterruptedException {

        new Thread(() -> {
            SocketClient socketClient = new SocketClient(config.socketPort, new SamplePipelineFactory());
            socketClient.start();
        }).start();

        while (true) {
            Thread.sleep(500);
        }
    }

}
