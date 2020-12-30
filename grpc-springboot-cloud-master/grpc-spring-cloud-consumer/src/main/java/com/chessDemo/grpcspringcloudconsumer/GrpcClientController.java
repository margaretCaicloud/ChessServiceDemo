package com.chessDemo.grpcspringcloudconsumer;

import com.chessDemo.grpcspringcloudconsumer.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/grpc")
public class GrpcClientController {

    @Autowired
    private GrpcClientService2 grpcClientService2;

    @Value("${grpcserver.port}")
    private Integer port;

    @RequestMapping("/{name}")
    public String printMessage(@PathVariable String name) {
        return grpcClientService2.sendMessage(name);
    }

//    @RequestMapping("/{flag}")
//    public String flow(@RequestParam(defaultValue = "GRPC Provider") String flag) {
//        return grpcClientService2.sendMessage(flag);
//    }

}
