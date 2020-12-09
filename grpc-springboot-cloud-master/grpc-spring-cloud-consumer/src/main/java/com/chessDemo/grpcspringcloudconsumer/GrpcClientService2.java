package com.chessDemo.grpcspringcloudconsumer;

import com.chessDemo.grpcspringcloudconsumer.config.GrpcServerPorperties;
import com.linshen.grpc.cloud.lib.GreeterGrpc;
import com.linshen.grpc.cloud.lib.GreeterOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

@Configuration
@Component
public class GrpcClientService2 {
    private static final Logger logger=Logger.getLogger(GrpcClientService2.class.getName());
    private static ManagedChannel serverChannel;
    private static GreeterGrpc.GreeterBlockingStub blockingStub;

    @Value("${grpcserver.host}")
    public String host;

    @Value("${grpcserver.port}")
    public int port;


    public void GrpcClientService2(String host,Integer port) {
        open();
    }

    private void open(){
        if(this.serverChannel==null) {
            this.serverChannel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
            blockingStub = GreeterGrpc.newBlockingStub(serverChannel);
        }
    }

    public void shutdown() throws InterruptedException {
        this.serverChannel.shutdown().awaitTermination(5, TimeUnit.MILLISECONDS);
    }

    public String sendMessage(String name) {

        GreeterOuterClass.HelloReply response;
        open();
        try{
            response = blockingStub.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
        }catch(StatusRuntimeException e){
            logger.log(Level.WARNING,"grpc failed:"+e.getMessage());
            return e.getMessage();
        }
        logger.info(response.getMessage());
        return response.getMessage();
    }
}
