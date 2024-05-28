package freeswitch.inbound;

import org.freeswitch.esl.client.transport.message.EslMessage;

import java.util.HashMap;
import java.util.Map;

public class RegisteredUser {
    HashMap<String,String> variables = new HashMap<String,String>();
    private final String callId;
    private final String user;
    private final String contact;
    private final String agent;
    private final String status;
    private final String pingStatus;
    private final String pingTime;
    private final String host;
    private final String ip;
    private final String port;
    private final String authUser;
    private final String authRealm;
    private final String mwiAccount;


    public RegisteredUser(EslMessage response){
        int len = response.getBodyLines().size();
//        for(int j= 3 ;j<len; j+=13) {
//
//        }
        for(int i =3; i<15; i++){
            String body = response.getBodyLines().get(i);

            // Split the line at the first occurrence of ":"
            int colonIndex = body.indexOf(':');
            if (colonIndex != -1) {
                String key = body.substring(0, colonIndex).trim();
                String value = body.substring(colonIndex + 1).trim();
                variables.put(key, value);
            }
        }


        this.callId = variables.get("Call-ID");
        this.user = variables.get("User");
        this.contact = variables.get("Contact");
        this.agent = response.getHeaders().get("Agent");
        this.status = variables.get("Status");
        this.pingStatus = variables.get("Ping-Status");
        this.pingTime = variables.get("Ping-Time");
        this.host = variables.get("Host");
        this.ip = variables.get("IP");
        this.port = variables.get("Port");
        this.authUser = variables.get("Auth-User");
        this.authRealm = variables.get("Auth-Realm");
        this.mwiAccount = variables.get("MWI-Account");
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "callId='" + callId + '\'' +
                ", user='" + user + '\'' +
                ", contact='" + contact + '\'' +
                ", agent='" + agent + '\'' +
                ", status='" + status + '\'' +
                ", pingStatus='" + pingStatus + '\'' +
                ", pingTime='" + pingTime + '\'' +
                ", host='" + host + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", authUser='" + authUser + '\'' +
                ", authRealm='" + authRealm + '\'' +
                ", mwiAccount='" + mwiAccount + '\'' +
                '}';
    }

}
