package freeswitch.controller;

import freeswitch.outbound.SampleOutboundHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FsApiExposer {
    @PostMapping("/getConcurrentCallCount")
    public int getConcurrentCallCount() {
        return SampleOutboundHandler.userMap.size();
    }
}
