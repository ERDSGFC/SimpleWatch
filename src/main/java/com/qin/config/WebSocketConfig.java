package com.qin.config;

import com.qin.websocket.*;
import com.qin.websocket.handshake.BaseHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    TestWebSocketHandler testWebSocketHandler;
    ChatHandler textHandler;
    BaseHandshakeInterceptor baseHandshakeInterceptor;

    @Autowired
    public void setBaseHandshakeInterceptor(final BaseHandshakeInterceptor baseHandshakeInterceptor) {
        this.baseHandshakeInterceptor = baseHandshakeInterceptor;
    }

    @Autowired
    public void setTestWebSocketHandler(final TestWebSocketHandler testWebSocketHandler) {
        this.testWebSocketHandler = testWebSocketHandler;
    }

    @Autowired
    public void setTextHandler(final ChatHandler textHandler) {
        this.textHandler = textHandler;
    }
    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(this.testWebSocketHandler, "/websocket/test")
                .addHandler(this.textHandler, "/websocket/chat")
                .addInterceptors(this.baseHandshakeInterceptor)
                .setAllowedOriginPatterns("localhost*")
//                .setAllowedOrigins("*");// 没有问题
                .setAllowedOriginPatterns("http://*");
//                .setAllowedOriginPatterns("*http://*.*:*").withSockJS(); 不要加 withSockJS();
//*https://*.domain1.com* -- 以domain1.com结尾的域名
//*https://*.domain1.com:[8080,8081]* -- domains ending with domain1.com on port 8080 or port 8081
//*https://*.domain1.com:[*]* -- domains ending with domain1.com on any port, including the default port
    }

//    @ServerEndpoint
//    通过这个 spring boot 就可以知道你暴露出去的 ws 应用的路径，有点类似我们经常用的@RequestMapping。比如你的启动端口是8080，而这个注解的值是ws，那我们就可以通过 ws://127.0.0.1:8080/ws 来连接你的应用
//    @OnOpen
//    当 websocket 建立连接成功后会触发这个注解修饰的方法，注意它有一个 Session 参数
//    @OnClose
//    当 websocket 建立的连接断开后会触发这个注解修饰的方法，注意它有一个 Session 参数
//    @OnMessage
//    当客户端发送消息到服务端时，会触发这个注解修改的方法，它有一个 String 入参表明客户端传入的值
//    @OnError
//    当 websocket 建立连接时出现异常会触发这个注解修饰的方法，注意它有一个 Session 参数
//    @Bean
//    public ServerEndpointExporter serverEndpoint() {
//        return new ServerEndpointExporter();
//    }

//    @Bean
//    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages.simpDestMatchers("/user/queue/errors").permitAll()
//                .simpDestMatchers("/admin/**").hasRole("ADMIN")
//                .anyMessage().authenticated();
//        return messages.build();
//    }

}
