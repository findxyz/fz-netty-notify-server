package xyz.fz.netty.notify.runner;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.netty.notify.server.NotifyServer;

@Component
public class NotifyServerRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        int mustArgLength = 2;
        if (args.length < mustArgLength) {
            System.out.println("Notify Server 服务器启动参数格式：host port");
            System.exit(0);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        NotifyServer notifyServer = new NotifyServer(host, port);
        notifyServer.run();
    }
}
