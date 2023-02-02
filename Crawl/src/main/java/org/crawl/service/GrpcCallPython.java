package org.crawl.service;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.crawl.proto.MsgProto;
import org.crawl.proto.MsgServiceGrpc;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GrpcCallPython {

    String host = "127.0.0.1";
    int port = 50051;

    public String testImgCal(String url,String body) {
        ManagedChannel channel=ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        MsgServiceGrpc.MsgServiceBlockingStub msgStub =MsgServiceGrpc.newBlockingStub(channel);

        MsgProto.MsgRequest msgRequest = MsgProto.MsgRequest.newBuilder()
                .setUrl(url).setBody(body)
                .build();
        MsgProto.MsgResponse msgResponse = msgStub.getMsg(msgRequest);
        return msgResponse.getMsg();
    }


}
