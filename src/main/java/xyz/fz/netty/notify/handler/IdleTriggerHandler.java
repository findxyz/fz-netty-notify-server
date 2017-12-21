package xyz.fz.netty.notify.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fz.netty.notify.model.NotifyMessage;
import xyz.fz.netty.notify.util.BaseUtil;
import xyz.fz.netty.notify.util.NettyChannelHelper;

public class IdleTriggerHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(IdleTriggerHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                // done close channel
                logger.warn("close idle client channel: {}", ctx.toString());
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                // done ping message
                logger.warn("ping client channel: {}", ctx.toString());
                NotifyMessage message = new NotifyMessage.Builder().pingMessage().build();
                ctx.channel().writeAndFlush(BaseUtil.toDelimiterJson(message));
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // done remove channel
        logger.warn("remove client channel: {}", ctx.toString());
        NettyChannelHelper.remove(ctx.channel());
        super.channelInactive(ctx);
    }
}
