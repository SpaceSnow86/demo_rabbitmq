package priv.ihch17;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 不使用交换机
 */
public class ProviderMain {
    public static void main(String[] args) {
        //  TODO 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //  TODO 设置rabbitMQ链接信息
        factory.setHost("127.0.0.1");   //  ip
        factory.setPort(5672);          //  port
        factory.setUsername("guest");   //  username
        factory.setPassword("guest");   //  password

        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();   //  获取链接
            channel = connection.createChannel();   //  通过链接获取通道

            //  TODO 声明1个队列
            //  注意：若队列存在，则自动放弃声明;若队列不存在，则声明队列
            channel.queueDeclare(
                    "myQueue",  //  队列名称，任意即可
                    true,       //  是否持久化队列
                    false,      //  是否只允许一个消费者监听
                    false,      //  若队列中没有消息，是否删除队列
                    null        //  其他设置，一般为null
            );

            //  TODO 准备一个消息
            String str = "This is my message";

            //  TODO 发送消息到Queue
            channel.basicPublish(
                    "",         //  交换机名称
                    "myQueue",   // 队列名称或RouteKey，当指定了交换机时，此处为RouteKey。当交换机名称为空时，此处为队列名称
                    null,   //  消息属性设置
                    str.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("消息发送成功！！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {

            //  TODO 关闭channel
            if(channel != null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

            //  TODO 关闭connection
            if (connection != null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
