package group.msg.springbootcds;

import group.msg.springbootcds.customize.HelloWorldSpringApplicationContextInitializer;
import group.msg.springbootcds.customize.HelloWorldSpringApplicationEventListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.Arrays;

@SpringBootApplication
@ImportRuntimeHints(ApplicationRuntimeHints.class)
public class SpringBootCdsApplication implements ApplicationRunner {

	public static void main(String[] args) {
		//		SpringApplication.run(Application.class, args);
		SpringApplication springApplication = new SpringApplication(SpringBootCdsApplication.class);

		springApplication.addListeners(new HelloWorldSpringApplicationEventListener());
		springApplication.addInitializers(new HelloWorldSpringApplicationContextInitializer());
		springApplication.setApplicationStartup(new BufferingApplicationStartup(1000));
		springApplication.run(args);
	}

	@Override
	public void run(ApplicationArguments args) {
		System.out.println("Here you can initialize your application ...");
		System.out.println(Arrays.toString(args.getSourceArgs()));
	}
}
