package xyz.fz.netty.notify.controller;

import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.netty.notify.model.NotifyMessage;
import xyz.fz.netty.notify.util.BaseUtil;
import xyz.fz.netty.notify.util.NettyChannelHelper;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/notify")
public class NotifyController {

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String, Object> push(@RequestBody Map<String, Object> message) {
        Map<String, Object> result = new HashMap<>();
        String fromId = message.get("fromId").toString();
        Object data = message.get("data");
        assert data instanceof Map;
        if (StringUtils.isBlank(fromId)) {
            result.put("success", false);
            result.put("message", "推送id及内容不可为空");
        } else {
            Channel channel = NettyChannelHelper.get(fromId);
            if (channel != null) {
                NotifyMessage notifyMessage = new NotifyMessage.Builder().message().from(fromId).data((Map<String, Object>) data).build();
                channel.writeAndFlush(BaseUtil.toDelimiterJson(notifyMessage));
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "当前推送对象不在线");
            }
        }
        return result;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> pushAll(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Channel> entry : NettyChannelHelper.map().entrySet()) {
            Channel channel = entry.getValue();
            NotifyMessage message = new NotifyMessage.Builder().message().data(data).build();
            channel.writeAndFlush(BaseUtil.toDelimiterJson(message));
        }
        result.put("success", true);
        return result;
    }
}
