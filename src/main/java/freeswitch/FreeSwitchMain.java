package freeswitch;

import freeswitch.cnfig.FreeSwitchConfig;
import freeswitch.inbound.InboundBuilder;
import freeswitch.outbound.OutboundBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FreeSwitchMain {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(FreeSwitchMain.class, args);
        FreeSwitchConfig instance1= new FreeSwitchConfig("192.168.0.123",8021,8084);
//        FreeSwitchConfig instance2= new FreeSwitchConfig("192.168.0.114",8021,8086);
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InboundBuilder.build(instance1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    OutboundBuilder.build(instance1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

        //InboundBuilder.build(instance1);
        //OutboundBuilder.build(instance1);


    }
}
