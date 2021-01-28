package priv.ihch17;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerConfirmMain {
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

            // TODO 消费者手动确认
            // 当消费者只接收了消息，但是还没有来得及处理，比如写入db时，db链接超时。但是发送者已经收到确认，删除了消息。导致业务失败。
            // 消费者手动确认模式，让MQ等待消费者的ack信息，获得ack信息后再删除消息
            channel.basicConsume(
                    "confirmQueue",
                    false,           // false使用手动确认
                    "",
                    new DefaultConsumer(channel)    //消息接收的回调方法，这个方法具体完成对消息的处理。
                    {
                        // 消息的具体接收和处理方法
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                            // TODO 防重复处理
                            //当前消息是否被接收过，如果返回true则表示该消息之前接收过
                            //所以要进行防重复处理
                            boolean flag = envelope.isRedeliver();

                            if(!flag){
                                String str = new String(body,"utf-8");
                                System.out.println(str);

                                // 获取当前编号
                                long tag = envelope.getDeliveryTag();

                                // 手动确认消息，确认后表示当前消息已经成功处理，可以从mq中移除
                                // 此方法应该在当前消息的处理程序全部完成后执行
                                this.getChannel().basicAck(tag,true);
                            }else{
                                // 此消息之前被接收过，需要进行防重复处理

                            }
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
