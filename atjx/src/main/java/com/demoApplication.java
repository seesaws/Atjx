package com;

import com.atjx.util.RunnableThreadWebCount;
import com.atjx.util.Timers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class demoApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(demoApplication.class, args);
        //计数线程
        RunnableThreadWebCount runnableThreadWebCount = new RunnableThreadWebCount();
        runnableThreadWebCount.run();
        //计时器线程
        Timers timers = new Timers();
        timers.run();

    }
}
