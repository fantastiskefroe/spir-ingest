package dk.fantastiskefroe.spir.ingest.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public final class CachingServerWebExchangeDecorator extends ServerWebExchangeDecorator {

    private final ServerHttpRequestDecorator requestDecorator;

    public CachingServerWebExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);

        this.requestDecorator = new CachingServerHttpRequestDecorator(delegate.getRequest());
    }

    @Override
    public ServerHttpRequest getRequest() {
        return requestDecorator;
    }

    private static class CachingServerHttpRequestDecorator extends ServerHttpRequestDecorator {

        private boolean firstSubscribe = true;
        private final List<byte[]> listByteArray = new ArrayList<>();

        CachingServerHttpRequestDecorator(ServerHttpRequest delegate) {
            super(delegate);
        }

        @Override
        public Flux<DataBuffer> getBody() {
            if (firstSubscribe) {
                return super.getBody().map(dataBuffer -> {
                    firstSubscribe = false;

                    ByteBuf copy = ((NettyDataBuffer) dataBuffer).getNativeBuffer().copy();
                    byte[] dst = new byte[copy.capacity()];
                    copy.readBytes(dst);
                    listByteArray.add(dst);
                    return dataBuffer;
                });
            } else {
                return Flux.fromIterable(listByteArray).map(this::getDataBuffer);
            }
        }

        private DataBuffer getDataBuffer(byte[] bytes) {
            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            return nettyDataBufferFactory.wrap(bytes);
        }
    }

}
