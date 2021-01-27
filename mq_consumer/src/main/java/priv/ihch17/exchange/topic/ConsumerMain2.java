package priv.ihch17.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerMain2 {
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

            // 交换机
            channel.exchangeDeclare("topicExchange","topic",true);

            // 队列
            channel.queueDeclare("topicQueue2",true,false,false,null);

            // 绑定
            channel.queueBind("topicQueue2","topicExchange","aa.*");

            //接收消息
            channel.basicConsume("topicQueue2",true,"",new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String str = new String(body);
                    System.out.println(str);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
