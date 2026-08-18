package com.mall.order.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 自定义 RabbitTemplate
     * 直接在创建时设置所有回调，避免循环依赖
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息转换器
        rabbitTemplate.setMessageConverter(messageConverter());

        // 开启 mandatory（路由失败时返回）
        rabbitTemplate.setMandatory(true);

        // 设置发布确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("confirm...correlationData[" + correlationData + "] ==> ack[" + ack + "] ==> cause[" + cause + "]");
        });

        // 设置返回回调（路由失败时触发）
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            System.out.println("Fail Message[" + returnedMessage.getMessage() + "]" +
                    "==>replyCode[" + returnedMessage.getReplyCode() + "]" +
                    "==>exchange[" + returnedMessage.getExchange() + "]" +
                    "==>routingKey[" + returnedMessage.getRoutingKey() + "]");
        });

        return rabbitTemplate;
    }
}
