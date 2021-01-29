package priv.ihch17.mq_springboot_provider.service.serviceImpl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.ihch17.mq_springboot_provider.service.ISendMessageService;

@Service("sendMessageService")
public class SendMessageService implements ISendMessageService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendMessage(String message) {

        /**
         * 发送消息
         * 参数1：交换机名称
         * 参数2：RoutingKey
         * 参数3：消息数据
         */
        amqpTemplate.convertAndSend("exchange_springboot_direct","routingkey_springboot_direct",message);
    }
}
