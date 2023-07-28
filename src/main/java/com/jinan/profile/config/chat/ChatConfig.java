package com.jinan.profile.config.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 클래스 상단에 @EnableWebSocketMessageBroker를 적고
 * implements로 WebSocketMessageBrokerConfigurer 인터페이스를 구현하면 stomp 설정을 할 수 있다.
 * 여기선 2개의 메서드를 @Override했다.
 */
@EnableWebSocketMessageBroker
@Configuration
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * stomp의 접속 주소 설정 (서버로 통신할때 사용할 엔드포인트를 정의한다.)
     * 클라이언트에서 sockJS를 이용할 것이기때문에 withSockJS()를 추가시켰다.
     * cors를 적용시키기 위해 setAllowedOriginPatterns("*")를 메서드 체이닝에 추가시킨다. -> *는 보안에 취약하니 나중에 꼭 바꿔주자
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 요청과 응답에 대한 엔드포인트를 설정한다.
     * 클라이언트에서 보내는 요청은 "/pub"이고 클라이언트에게 메시지를 보내는 응답은 "/sub"이다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독하는 클라이언트에게 메시지 전달 엔드포인트를 더 추가해서 구독을 여러개 할수도 있다.
        registry.enableSimpleBroker("/subscribe");
        // 클라이언트의 전송요청을 처리
        registry.setApplicationDestinationPrefixes("/publish");

    }

}
