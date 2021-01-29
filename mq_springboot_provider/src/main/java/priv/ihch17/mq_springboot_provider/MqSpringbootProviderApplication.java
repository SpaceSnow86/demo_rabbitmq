package priv.ihch17.mq_springboot_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import priv.ihch17.mq_springboot_provider.service.serviceImpl.SendMessageService;

@SpringBootApplication
public class MqSpringbootProviderApplication {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(MqSpringbootProviderApplication.class, args);
        SendMessageService sendMessageService = (SendMessageService) ac.getBean("sendMessageService");
        sendMessageService.sendMessage("这是springboot direct测试数据");
    }

}
