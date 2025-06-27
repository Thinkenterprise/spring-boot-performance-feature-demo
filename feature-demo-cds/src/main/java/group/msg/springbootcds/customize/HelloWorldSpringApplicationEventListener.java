package group.msg.springbootcds.customize;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class HelloWorldSpringApplicationEventListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("Hello World Application Initalizeation: Receive Initalizeation Message " + event.toString());
    }
}