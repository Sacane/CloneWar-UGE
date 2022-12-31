package fr.ramatellier.clonewar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.print.attribute.standard.Media;
import java.util.concurrent.Executors;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@ComponentScan
@EnableWebFlux
public class AppConfig {
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public Scheduler schedulerCtx(){
        return Schedulers.fromExecutor(Executors.newSingleThreadExecutor());
    }

    @Bean
    public RouterFunction<ServerResponse> htmlRouter(
            @Value("classpath:/static/index.html") Resource html
    ){
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(html));
    }
}
