package xyz.fz.netty.notifyServer.util;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fz.netty.notifyServer.model.NotifyMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelHelper {

    private static final Logger logger = LoggerFactory.getLogger(NettyChannelHelper.class);

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static Map<String, Channel> map() {
        return channelMap;
    }

    public static void add(String id, Channel channel) {
        Channel c = channelMap.get(id);
        if (c != null) {
            NotifyMessage message = new NotifyMessage.Builder().closeMessage().build();
            c.writeAndFlush(BaseUtil.toDelimiterJson(message));
            logger.warn("kick old client channel: {}", c.toString());
        }
        channelMap.put(id, channel);
    }

    public static Channel get(String id) {
        return channelMap.get(id);
    }

    public static void remove(Channel channel) {
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel c = entry.getValue();
            if (c == channel) {
                channelMap.remove(entry.getKey());
            }
        }
    }
}
