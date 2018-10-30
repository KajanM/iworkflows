package com.kajan.iworkflows.sample;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AddDummyCars {

    @Bean
    ApplicationRunner init(CarRepository carRepository) {
        return args -> {
            Stream.of("Ferrari", "Jaguar", "Porsche", "Lamborghini", "Bugatti",
                    "AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV").forEach(name -> {
                Car car = new Car();
                car.setName(name);
                carRepository.save(car);
            });
            //carRepository.findAll().forEach(System.out::println);
        };
    }
}
