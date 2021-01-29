package priv.ihch17.mq_springboot_provider.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  rabbitMQ配置类
 * */
@Configuration
public class RabbitMQConfiguration {

    //配置一个direct类型的交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("exchange_springboot_direct");
    }

    //配置一个queue
    @Bean
    public Queue directQueue(){
        return  new Queue("queue_springboot");
    }

    // 绑定队列和交换机
    @Bean
    public Binding binding(){
        // bind 交换机定义方法 to queue定义方法 with routingkey
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("routingkey_springboot_direct");
    }
}
