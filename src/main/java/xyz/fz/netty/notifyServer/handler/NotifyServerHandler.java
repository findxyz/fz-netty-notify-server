package xyz.fz.netty.notifyServer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fz.netty.notifyServer.model.NotifyMessage;
import xyz.fz.netty.notifyServer.util.BaseUtil;
import xyz.fz.netty.notifyServer.util.NettyChannelHelper;

public class NotifyServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(NotifyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        BaseUtil.scheduledExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // done client message
                logger.warn("client message: {}", s);
                NotifyMessage message = BaseUtil.parseJson(s, NotifyMessage.class);
                if (message != null) {
                    switch (message.getType()) {
                        case NotifyMessage.CONNECT:
                            // done connect channel
                            logger.warn("connect client channel: {}", ctx.toString());
                            NettyChannelHelper.add(message.getFromId(), ctx.channel());
                            break;
                        case NotifyMessage.PING:
                            break;
                        case NotifyMessage.PONG:
                            break;
                        case NotifyMessage.MESSAGE:
                            break;
                        case NotifyMessage.CLOSE:
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("close exception client channel: {}", ctx.toString());
        cause.printStackTrace();
        ctx.close();
    }
}
