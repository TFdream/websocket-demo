package websocket.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.netty.manager.UserChannelManager;
import websocket.netty.util.NettyUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Ricky Fung
 */
public class HeartbeatHandler extends IdleStateHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public HeartbeatHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent event) throws Exception {
        if (event == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
            LOG.info("检测到客户端空闲，关闭连接, channelId={}, clientIp={}", NettyUtils.getChannelId(ctx), NettyUtils.getClientIp(ctx));
            Channel channel = ctx.channel();
            // Clear cache
            UserChannelManager.getInstance().remove(channel);
            channel.close();
            return;
        }
        super.channelIdle(ctx, event);
    }
}
