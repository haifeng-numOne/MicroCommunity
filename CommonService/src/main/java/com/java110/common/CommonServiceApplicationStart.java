package com.java110.common;

import com.java110.common.activity.DeploymentActivity;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.common",
        "com.java110.core", "com.java110.cache", "com.java110.config.properties.code", "com.java110.db"},
        exclude = {LiquibaseAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.service.common.listener"})
@EnableFeignClients(basePackages = {"com.java110.core.smo.user"})
public class CommonServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceApplicationStart.class);


    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        return restTemplate;
    }

    /**
     * 实例化RestTemplate
     *
     * @return restTemplate
     */
    @Bean
    public com.java110.core.client.RestTemplate restTemplateNoLoadBalanced() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        com.java110.core.client.RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(com.java110.core.client.RestTemplate.class);
        return restTemplate;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(CommonServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //初始化 activity 流程
        //DeploymentActivity.deploymentProcess();
    }
}