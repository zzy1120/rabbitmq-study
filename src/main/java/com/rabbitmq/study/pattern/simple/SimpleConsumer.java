package com.rabbitmq.study.pattern.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @ClassName Consumer
 * @Description: TODO
 * @Author zzy
 * @Date 2021/5/8
 **/
public class SimpleConsumer {

    public static void main(String[] args) {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2、设置连接属性
        connectionFactory.setHost("192.168.75.150");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = null;
        Channel channel = null;

        try {
            //3、从连接工厂中获取连接
            connection = connectionFactory.newConnection("生产者");
            //4、从连接中获取通道channel
            channel = connection.createChannel();
            //5、通过通道创建交换机，声明队列，绑定关系，路由key,发送消息，和接收消息
            String queueName = "queue1";
            channel.queueBind(queueName, "", "");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    //消息内容
                    System.out.println("简单队列模式，消费端msg：" + msg);
                }
            };
            System.out.println("消息接收成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("消息接收异常");
        }
    }
}
