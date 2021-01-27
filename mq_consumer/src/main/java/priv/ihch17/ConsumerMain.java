package priv.ihch17;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 不使用交换机
 */
public class ConsumerMain {
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

            //  TODO 接收消息
            channel.basicConsume(
                    "myQueue",      //  需要监听的队列名称
                    true,           //  消息是否自动确认  true表示消息接收后自动删除
                    "",             //  消息接受者标签，用于当多个消费者监听同一个队列时用于区分不同的消费者，通常使用空字符串即可
                    new DefaultConsumer(channel)    //消息接收的回调方法，这个方法具体完成对消息的处理。
                    {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            //消息的具体接收和处理方法
                            String str = new String(body,"utf-8");
                            System.out.println(str);
                        }
                    }
            );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
