package priv.ihch17.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerMain1 {
    public static void main(String[] args){

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

            String queueName = channel.queueDeclare().getQueue();
            channel.exchangeDeclare("fanoutExchange","fanout",true);
            channel.queueBind(queueName,"fanoutExchange","");

            //监听队列
            channel.basicConsume(queueName,true,"",new DefaultConsumer(channel){
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
