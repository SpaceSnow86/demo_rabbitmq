package priv.ihch17;

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

            channel.exchangeDeclare("transactionExchange","direct",true);
            channel.queueDeclare("transactionQueue",true,false,false,null);
            channel.queueBind("transactionQueue","transactionExchange","transactionRoutingKey");

            // TODO 开启事务
            channel.txSelect();
            //发送消息1
            String str = "事务测试消息1";
            channel.basicPublish("transactionExchange","transactionRoutingKey",null,str.getBytes(StandardCharsets.UTF_8));

            // 发送消息1
            String str2 = "事务测试消息2";

            // 出错代码
            //int a = 10 / 0;
            channel.basicPublish("transactionExchange","transactionRoutingKey",null,str2.getBytes(StandardCharsets.UTF_8));

            // TODO 提交事务
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                // TODO 回滚事务
                channel.txRollback();
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
