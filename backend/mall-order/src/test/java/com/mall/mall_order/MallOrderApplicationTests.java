package com.mall.mall_order;

import com.alibaba.druid.sql.visitor.functions.Bin;
import com.mall.order.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Slf4j
class MallOrderApplicationTests {

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Test
	public void createExchange(){
		DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
		amqpAdmin.declareExchange(directExchange);
		log.info("Exchange[{}]创建成功", "hello-java-exchange");
	}

	@Test
	public void createQueue(){
		Queue queue = new Queue("hello-java-queue", true, false, false);
		amqpAdmin.declareQueue(queue);
		log.info("Queue[{}]创建成功", "hello-java-queue");
	}

	@Test
	public void createBinding(){
		Binding binding = new Binding("hello-java-queue",
				Binding.DestinationType.QUEUE,
				"hello-java-exchange",
				"hello.java", null);
		amqpAdmin.declareBinding(binding);
		log.info("Binding[{}]创建成功", "hello-java-binding");
	}

	@Test
	public void sendMessageTest(){
		OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
		reasonEntity.setId(1L);
		reasonEntity.setCreateTime(new Date());
		reasonEntity.setName("哈哈");
		String msg = "Hello World!";
		rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", reasonEntity);
		log.info("消息发送完成{}", reasonEntity);
	}
}
