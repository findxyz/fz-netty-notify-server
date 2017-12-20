package xyz.fz.netty.notifyServer.runner;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.netty.notifyServer.server.NotifyServer;

@Component
public class NotifyServerRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        if (args.length < 2) {
            System.out.println("Notify Server 服务器启动参数格式：host port");
            System.exit(0);
        }

        // done 从消息源获得消息后推送，使用controller进行推送
        /*
        BaseUtil.scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Channel> entry : NettyChannelHelper.map().entrySet()) {
                    Channel channel = entry.getValue();
                    Map<String, Object> data = new HashMap<>();
                    data.put("a", "1");
                    data.put("b", "2");
                    NotifyMessage message = new NotifyMessage.Builder().message().data(data).build();
                    channel.writeAndFlush(BaseUtil.toDelimiterJson(message));
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
        */

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        NotifyServer notifyServer = new NotifyServer(host, port);
        notifyServer.run();
    }
}
