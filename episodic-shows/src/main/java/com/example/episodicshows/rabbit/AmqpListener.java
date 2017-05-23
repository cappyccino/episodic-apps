package com.example.episodicshows.rabbit;

import com.example.episodicshows.viewings.ViewingService;
import com.example.episodicshows.viewings.ViewingWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AmqpListener implements RabbitListenerConfigurer {

    @Autowired
    ViewingService viewingService;

    @RabbitListener(queues = "episodic-progress")
    @Transactional
    public void receiveMessage(final ProgressMessage progressMessage) {
        ViewingWrapper data = new ViewingWrapper(
                progressMessage.getEpisodeId(),
                progressMessage.getCreatedAt(),
                progressMessage.getOffset());

        viewingService.createOrUpdateViewing(progressMessage.getUserId(), data);
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

}
