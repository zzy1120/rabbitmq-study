package com.rabbitmq.study.pattern.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName Producer
 * @Description: TODO
 * @Author zzy
 * @Date 2021/5/8
 **/
public class Producer {

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

            /**
             * @params1 队列的名称
             * @params2 是否要持久化 如果false 非持久，true是持久
             * @params1 排他性，是否是独占独立
             * @params1 是否自动删除，随着最后一个消费者消费完毕消息以后是否把队列自动删除
             * @params1 携带附属参数
             */
            channel.queueDeclare(queueName, true, false, false, null);

            //6、准备发送消息的内容
            String message = "hello zzy!!";
            //7、发送消息给队列
            /**
             *  @params1: 交换机
             *  @params2: 队列或者路由key
             *  @params3: 消息的状态控制
             *  @params4: 消息主题
             *  可以存在没有交换机的队列吗？
             *      不可能，虽然没有指定交换机，但是一定会存在一个默认的交换机
             */

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println("消息发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("消息发送异常");
        } finally {
            //8、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
