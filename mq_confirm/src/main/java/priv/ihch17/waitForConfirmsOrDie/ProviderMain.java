package priv.ihch17.waitForConfirmsOrDie;

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

            channel.exchangeDeclare("confirmOrDieExchange","direct",true);
            channel.queueDeclare("confirmOrDieQueue",true,false,false,null);
            channel.queueBind("confirmOrDieQueue","confirmOrDieExchange","confirmOrDieRoutingKey");

            // 启动发送者确认模式
            channel.confirmSelect();

            //发送消息
            String str = "发送者确认模式-批量确认模式-测试消息1";
            channel.basicPublish("confirmOrDieExchange","confirmOrDieRoutingKey",null,str.getBytes(StandardCharsets.UTF_8));

            //批量确认
            //没有返回值
            //效率比普通确认高，但是如果消息出现问题，无法指定补发消息，只能全部补发
            channel.waitForConfirmsOrDie();
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
