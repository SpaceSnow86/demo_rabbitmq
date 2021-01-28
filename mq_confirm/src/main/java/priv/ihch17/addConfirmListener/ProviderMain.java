package priv.ihch17.addConfirmListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.org.apache.bcel.internal.generic.NEW;

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

            channel.exchangeDeclare("addConfirmListenerExchange","direct",true);
            channel.queueDeclare("addConfirmListenerQueue",true,false,false,null);
            channel.queueBind("addConfirmListenerQueue","addConfirmListenerExchange","addConfirmListenerRoutingKey");

            // 启动发送者确认模式
            channel.confirmSelect();

            //发送消息
            String str = "发送者确认模式-异步监听-测试消息1";
            channel.basicPublish("addConfirmListenerExchange","addConfirmListenerRoutingKey",null,str.getBytes(StandardCharsets.UTF_8));

            //异步监听确认
            channel.addConfirmListener(new ConfirmListener() {

                // 消息确认回调方法
                // 参数1：消息被确认的编号，从1开始
                // 参数2：是否同时确认了多条消息
                @Override
                public void handleAck(long l, boolean b) throws IOException {

                }

                // 消息没确认回调方法
                // 参数1：消息没有被确认的编号，从1开始
                // 参数2：是否同时没有被确认多条消息 
                @Override
                public void handleNack(long l, boolean b) throws IOException {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
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
