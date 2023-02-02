package org.crawl.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.32.1)",
    comments = "Source: msg.proto")
public final class MsgServiceGrpc {

  private MsgServiceGrpc() {}

  public static final String SERVICE_NAME = "msg.MsgService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.crawl.proto.MsgProto.MsgRequest,
      org.crawl.proto.MsgProto.MsgResponse> getGetMsgMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMsg",
      requestType = org.crawl.proto.MsgProto.MsgRequest.class,
      responseType = org.crawl.proto.MsgProto.MsgResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.crawl.proto.MsgProto.MsgRequest,
      org.crawl.proto.MsgProto.MsgResponse> getGetMsgMethod() {
    io.grpc.MethodDescriptor<org.crawl.proto.MsgProto.MsgRequest, org.crawl.proto.MsgProto.MsgResponse> getGetMsgMethod;
    if ((getGetMsgMethod = MsgServiceGrpc.getGetMsgMethod) == null) {
      synchronized (MsgServiceGrpc.class) {
        if ((getGetMsgMethod = MsgServiceGrpc.getGetMsgMethod) == null) {
          MsgServiceGrpc.getGetMsgMethod = getGetMsgMethod =
              io.grpc.MethodDescriptor.<org.crawl.proto.MsgProto.MsgRequest, org.crawl.proto.MsgProto.MsgResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMsg"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.crawl.proto.MsgProto.MsgRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.crawl.proto.MsgProto.MsgResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MsgServiceMethodDescriptorSupplier("GetMsg"))
              .build();
        }
      }
    }
    return getGetMsgMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MsgServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MsgServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MsgServiceStub>() {
        @java.lang.Override
        public MsgServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MsgServiceStub(channel, callOptions);
        }
      };
    return MsgServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MsgServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MsgServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MsgServiceBlockingStub>() {
        @java.lang.Override
        public MsgServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MsgServiceBlockingStub(channel, callOptions);
        }
      };
    return MsgServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MsgServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MsgServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MsgServiceFutureStub>() {
        @java.lang.Override
        public MsgServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MsgServiceFutureStub(channel, callOptions);
        }
      };
    return MsgServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static abstract class MsgServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public void getMsg(org.crawl.proto.MsgProto.MsgRequest request,
        io.grpc.stub.StreamObserver<org.crawl.proto.MsgProto.MsgResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMsgMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMsgMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.crawl.proto.MsgProto.MsgRequest,
                org.crawl.proto.MsgProto.MsgResponse>(
                  this, METHODID_GET_MSG)))
          .build();
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceStub extends io.grpc.stub.AbstractAsyncStub<MsgServiceStub> {
    private MsgServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MsgServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MsgServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public void getMsg(org.crawl.proto.MsgProto.MsgRequest request,
        io.grpc.stub.StreamObserver<org.crawl.proto.MsgProto.MsgResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMsgMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MsgServiceBlockingStub> {
    private MsgServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MsgServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MsgServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public org.crawl.proto.MsgProto.MsgResponse getMsg(org.crawl.proto.MsgProto.MsgRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMsgMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MsgServiceFutureStub> {
    private MsgServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MsgServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MsgServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.crawl.proto.MsgProto.MsgResponse> getMsg(
        org.crawl.proto.MsgProto.MsgRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMsgMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MSG = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MsgServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MsgServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MSG:
          serviceImpl.getMsg((org.crawl.proto.MsgProto.MsgRequest) request,
              (io.grpc.stub.StreamObserver<org.crawl.proto.MsgProto.MsgResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MsgServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MsgServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.crawl.proto.MsgProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MsgService");
    }
  }

  private static final class MsgServiceFileDescriptorSupplier
      extends MsgServiceBaseDescriptorSupplier {
    MsgServiceFileDescriptorSupplier() {}
  }

  private static final class MsgServiceMethodDescriptorSupplier
      extends MsgServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MsgServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MsgServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MsgServiceFileDescriptorSupplier())
              .addMethod(getGetMsgMethod())
              .build();
        }
      }
    }
    return result;
  }
}
