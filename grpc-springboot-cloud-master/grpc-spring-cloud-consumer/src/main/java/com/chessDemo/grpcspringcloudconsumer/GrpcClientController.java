package com.chessDemo.grpcspringcloudconsumer;

import com.chessDemo.grpcspringcloudconsumer.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/grpc")
public class GrpcClientController {

    @Autowired
    private GrpcClientService grpcClientService;

    @RequestMapping("/{name}")
    public String printMessage(@RequestParam(defaultValue = "GRPC Provider") String name) {
        return grpcClientService.sendMessage(name);
    }

}
