package cn.edu.fc;

import cn.edu.fc.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cn.edu.fc.javaee.core",
        "cn.edu.fc.schedulesystem.forecast"})
@ComponentScan(basePackages = {"cn.edu.fc.*"})
@EnableJpaRepositories(value = {"cn.edu.fc.dao"}, repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.fc.mapper")
@EntityScan("cn.edu.fc.*")
@EnableFeignClients
@EnableDiscoveryClient
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}