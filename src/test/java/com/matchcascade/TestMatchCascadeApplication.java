package com.matchcascade;

import org.springframework.boot.SpringApplication;

public class TestMatchCascadeApplication {

    public static void main(String[] args) {
        SpringApplication.from(MatchCascadeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
