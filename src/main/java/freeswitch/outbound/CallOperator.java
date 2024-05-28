package freeswitch.outbound;

import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.internal.AbstractEslClientHandler;
import org.freeswitch.esl.client.transport.SendMsg;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CallOperator extends AbstractEslClientHandler {
        private static Map<String, String> map = new HashMap<String, String>();
    static {
        map = getDatabaseMap();
    }

    public static Map<String, String> getDatabaseMap() {
        Map<String, String> map = new HashMap<>();

        String url = "jdbc:mysql://localhost:3306/ccl_mnp";
        String user = "root";
        String password = "1234";
        String query = "SELECT number, recepeintRC FROM tb_mnp";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String key = resultSet.getString(1); // First column as key
                String value = resultSet.getString(2); // Second column as value
                map.put(key, value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public void bridgeCall(Channel channel,String destNumber) {

        SendMsg bridgeMsg = new SendMsg();
        bridgeMsg.addCallCommand("execute");
        bridgeMsg.addExecuteAppName("bridge");
        bridgeMsg.addExecuteAppArg("user/" + destNumber);

        //
        Map<String, String> map = getDatabaseMap();
        if(map.containsKey(destNumber)){
            //do something
        }
        else{
            EslMessage response = sendSyncMultiLineCommand(channel, bridgeMsg.getMsgLines());
            if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
                String originCall = destNumber;
                System.out.println(originCall + " bridge to " + destNumber + " successful");
            } else {
                System.out.println("Call bridge failed: " + response.getHeaderValue(EslHeaders.Name.REPLY_TEXT));
            }
        }
        //


    }
    private void playSound(Channel channel, EslEvent event) {
        String uuid = event.getEventHeaders().get("Unique-ID");
        SendMsg playbackMsg = new SendMsg(uuid);
        playbackMsg.addCallCommand("execute");
        playbackMsg.addExecuteAppName("playback");
        EslMessage response = sendSyncMultiLineCommand(channel, playbackMsg.getMsgLines());
        if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
            System.out.println("playback successful");
        } else {
            System.out.println("playback failed: " + response.getHeaderValue(EslHeaders.Name.REPLY_TEXT));
        }
    }

    private void hangupCall(Channel channel) {
        SendMsg hangupMsg = new SendMsg();
        hangupMsg.addCallCommand("execute");
        hangupMsg.addExecuteAppName("hangup");

        EslMessage response = sendSyncMultiLineCommand(channel, hangupMsg.getMsgLines());
        if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
            System.out.println("Call hangup successful");
        } else {
            System.out.println("Call hangup failed: " + response.getHeaderValue(EslHeaders.Name.REPLY_TEXT));
        }
    }

    public void unparkCall(Channel channel,EslEvent e) throws InboundConnectionFailure {
        String uuid = e.getEventHeaders().get("Unique-ID");
        String description = e.getEventHeaders().get("Channel-Destination-Number");
        String command = String.format("uuid_transfer "+ uuid+" "+ description+" XML after_esl");
        Client client = new Client();
        client.connect("192.168.0.150", 8021, "ClueCon", 10);

        EslMessage response = client.sendSyncApiCommand(command, "");
        System.out.println(command);
    }


    protected  void handleEslEvent(ChannelHandlerContext ctx, EslEvent event ){};

    protected  void handleAuthRequest( ChannelHandlerContext ctx ){};

    protected  void handleDisconnectionNotice(){};
}
