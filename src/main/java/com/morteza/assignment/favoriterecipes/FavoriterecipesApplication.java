package com.morteza.assignment.favoriterecipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class FavoriterecipesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavoriterecipesApplication.class, args);
    }

}
