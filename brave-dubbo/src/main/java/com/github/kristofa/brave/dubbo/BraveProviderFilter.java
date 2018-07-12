package com.github.kristofa.brave.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.*;
import com.twitter.zipkin.gen.Span;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * Created by chenjg on 16/7/24.
 */

 class ServerRequestAdapterImpl implements ServerRequestAdapter {

    Random randomGenerator = new Random();
    SpanId spanId;
    String spanName;
    public static SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder()
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }
    ServerRequestAdapterImpl(String spanName){
        this.spanName = spanName;
        long startId = randomGenerator.nextLong();
        SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
        this.spanId = spanId;
    }

    ServerRequestAdapterImpl(String spanName, SpanId spanId){
        this.spanName = spanName;
        this.spanId = spanId;
}

    @Override
    public TraceData getTraceData() {
        if (this.spanId != null) {
            return TraceData.builder().spanId(this.spanId).build();
        }
        long startId = randomGenerator.nextLong();
        SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
        return TraceData.builder().spanId(spanId).build();
    }

    @Override
    public String getSpanName() {
        return spanName;
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
        KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
        collection.add(kv);
        return collection;
    }

}

 class ServerResponseAdapterImpl implements ServerResponseAdapter {

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
        KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
        collection.add(kv);
        return collection;
    }

}


@Activate(group = Constants.PROVIDER)
public class BraveProviderFilter implements Filter {


    private static volatile Brave brave;
    private static volatile ServerRequestInterceptor serverRequestInterceptor;
    private static volatile ServerResponseInterceptor serverResponseInterceptor;
    private static volatile ServerSpanThreadBinder serverSpanThreadBinder;



    public static void setBrave(Brave brave) {
        BraveProviderFilter.brave = brave;
        BraveProviderFilter.serverRequestInterceptor = brave.serverRequestInterceptor();
        BraveProviderFilter.serverResponseInterceptor = brave.serverResponseInterceptor();
        BraveProviderFilter.serverSpanThreadBinder = brave.serverSpanThreadBinder();
    }


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(invoker,invocation,brave.serverTracer()));

        /*final String parentId = invocation.getAttachment("parentId");
        final String spanId = invocation.getAttachment("spanId");
        final String traceId = invocation.getAttachment("traceId");
        SpanId spanIds =ServerRequestAdapterImpl.getSpanId(traceId,spanId,parentId);

        serverRequestInterceptor.handle(new ServerRequestAdapterImpl("oooo", spanIds));*/
        Result rpcResult = invoker.invoke(invocation);
       // serverResponseInterceptor.handle(new ServerResponseAdapterImpl());
          serverResponseInterceptor.handle(new DubboServerResponseAdapter(rpcResult));






          return rpcResult;
    }


}
