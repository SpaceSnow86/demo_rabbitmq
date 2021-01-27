package priv.ihch17.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProviderMain {

    public static void main(String[] args) {

        // TODO 链接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //  TODO    链接信息
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        //  TODO    通过工厂创建connection
        Connection connection = null;

        // TODO 通过connection创建channel
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            // TODO 声明队列
            channel.queueDeclare(
                    "myDirectQueue",
                    true,
                    false,
                    false,
                    null
            );

            // TODO 声明交换机
            //注：声明交换机时如果交换机已经存在，则放弃声明；如果不存在，则声明
            channel.exchangeDeclare(
                    "directExchange",     //交换机名称
                    "direct",     //交换机类型:direct\fanout\topic\headers
                    true      //是否持久化交换机
            );

            // TODO 绑定交换
            channel.queueBind(
                    "myDirectQueue",        //队列名称
                    "directExchange",       //交换机名称
                    "direct_routintKey"     //routint key
            );

            String str = "tihis is direct exchange!";

            // TODO 发送消息
            channel.basicPublish(
                    "directExchange" ,     // 交换机名称
                    "direct_routintKey",    // routing key
                    null,
                    str.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("发送成功");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {

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
