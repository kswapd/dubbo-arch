package apps;

import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.dubbo.BraveFactoryBean;
import com.github.kristofa.brave.dubbo.DubboServerNameProvider;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static jline.ConsoleRunner.property;

/**
 * Created by kongxiangwen on 5/15/18 w:20.
 */
@Configuration
public class ServerMain{
		public static void main(String[] args) throws IOException {

			System.setProperty("java.net.preferIPv4Stack", "true");
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationProvider.xml"});


			context.start();
			System.out.println("输入任意按键退出 ~ ");
			System.in.read();
			//context.close();
		}

		@Bean(name="brave")
		public BraveFactoryBean getBrave()
		{
			/*<bean id="brave" class="com.github.kristofa.brave.dubbo.BraveFactoryBean">

        <property name="serviceName">
            <value>helloworld-provider</value>
        </property>
        <property name="zipkinHost">
            <value>http://${zipkin.address}:${zipkin.port}/</value>
        </property>
        <property name="rate">
            <value>${zipkin.sampleRate}</value>
        </property>
    </bean>*/
				//String application = RpcContext.getContext().getUrl().getParameter("application");

				System.out.println(RpcContext.getContext().getUrl());
				BraveFactoryBean bfb = new BraveFactoryBean();
				bfb.setZipkinHost("http://10.88.2.112:9411/");
				bfb.setRate("1.0");
				bfb.setServiceName("hello-provider");
				return bfb;

		}



}
