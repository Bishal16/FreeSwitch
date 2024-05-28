package freeswitch.inbound;

import freeswitch.cnfig.FreeSwitchConfig;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;


public class InboundBuilder {


    public static void build(FreeSwitchConfig config) throws InterruptedException {
        Client client = new Client();
        try {
            client.connect(config.eslIp, config.port, "ClueCon", 10);
            System.out.println("Successfully connected to " + config.eslIp);
            String command = "sofia status profile external reg";
            EslMessage response = client.sendSyncApiCommand(command, "");
            RegisteredUser registered = new RegisteredUser(response);
            client.addEventListener(new IEslEventListener() {

                @Override
                public void eventReceived(EslEvent event) {
                    String eventName = event.getEventName();
                    if (eventName.startsWith("CHANNEL_")) {
                        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
                        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
                        switch (eventName) {
                            case "CHANNEL_CREATE":
                                System.out.println("Creat Channel " + callerNumber + "to：" + calleeNumber);
                                break;
                            case "CHANNEL_BRIDGE":
                                System.out.println("Bridge call " + callerNumber + "to：" + calleeNumber);
                                break;
                            case "CHANNEL_ANSWER":
                                System.out.println("Answer call：" + callerNumber + " to：" + calleeNumber);
                                break;
                            case "CHANNEL_HANGUP":
                                String response = event.getEventHeaders().get("variable_current_application_response");
                                String hangupCause = event.getEventHeaders().get("Hangup-Cause");
                                System.out.println("End Call" + callerNumber + " to：" + calleeNumber + " , response:" + response + " ,hangup cause:" + hangupCause);
                                break;
                            default:
                                break;
                        }
                    }
                }

                @Override
                public void backgroundJobResultReceived(EslEvent event) {
                    String jobUuid = event.getEventHeaders().get("Job-UUID");
                    System.out.println("异步回调:" + jobUuid);
                }
            });

        } catch (InboundConnectionFailure inboundConnectionFailure) {
            System.out.println("连接失败！");
            inboundConnectionFailure.printStackTrace();
        }

    }
}
