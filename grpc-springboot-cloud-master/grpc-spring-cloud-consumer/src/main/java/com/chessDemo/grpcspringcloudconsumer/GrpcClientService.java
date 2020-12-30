package com.chessDemo.grpcspringcloudconsumer;

//import com.linshen.grpc.cloud.lib.GreeterGrpc;
//import com.linshen.grpc.cloud.lib.GreeterOuterClass;
//import io.grpc.Channel;
//import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;

//@Service
//public class GrpcClientService {
//
//    @GrpcClient("localhost:50052")//grpc-spring-cloud-provider
//    private Channel serverChannel;
//
//
//    public String sendMessage(String name) {
//        GreeterGrpc.GreeterBlockingStub stub= GreeterGrpc.newBlockingStub(serverChannel);
//        GreeterOuterClass.HelloReply response = stub.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
//        return response.getMessage();
//    }
//}
