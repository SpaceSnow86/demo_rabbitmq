package priv.ihch17.waitForConfirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProviderMain {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare("confirmExchange","direct",true);
            channel.queueDeclare("confirmQueue",true,false,false,null);
            channel.queueBind("confirmQueue","confirmExchange","confirmRoutingKey");

            // 启动发送者确认模式
            channel.confirmSelect();

            //发送消息
            String str = "发送者确认模式-普通模式-测试消息1";
            channel.basicPublish("confirmExchange","confirmRoutingKey",null,str.getBytes(StandardCharsets.UTF_8));

            //普通确认
            //阻塞线程等待程序响应，若确认消息发送完成，返回true;否则返回false
            //可以为这个方法制定一个毫秒级时间，超过次时间测抛出异常。
            boolean flag = channel.waitForConfirms();
            System.out.println(flag);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
