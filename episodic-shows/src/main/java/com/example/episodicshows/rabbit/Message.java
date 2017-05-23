package com.example.episodicshows.rabbit;

import lombok.Data;

@Data
public class Message {
    private final String hello;

    @Override
    public String toString() {
        return "HelloMessage{" +
                "hello='" + hello + '\'' +
                '}';
    }
}
