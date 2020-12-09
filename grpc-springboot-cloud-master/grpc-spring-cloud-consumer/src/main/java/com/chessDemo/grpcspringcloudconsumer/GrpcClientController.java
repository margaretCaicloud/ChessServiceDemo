package com.chessDemo.grpcspringcloudconsumer;

import com.chessDemo.grpcspringcloudconsumer.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/grpc")
public class GrpcClientController {

    @Autowired
    private GrpcClientService2 grpcClientService2;

    @Value("${grpcserver.port}")
    private Integer port;

    @RequestMapping("/{name}")
    public String printMessage(@RequestParam(defaultValue = "GRPC Provider") String name) {
        return grpcClientService2.sendMessage(name);
    }

}
