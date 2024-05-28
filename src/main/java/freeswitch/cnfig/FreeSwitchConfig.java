package freeswitch.cnfig;

public class FreeSwitchConfig {
    public final String eslIp;
    public final int port;
    public final int socketPort;

    public FreeSwitchConfig(String eslIp, int port,int socketPort) {
        this.eslIp = eslIp;
        this.port = port;
        this.socketPort = socketPort;
    }
}
