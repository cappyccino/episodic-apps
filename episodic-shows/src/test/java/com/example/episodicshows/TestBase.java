package com.example.episodicshows;

import org.junit.AfterClass;
import org.junit.ClassRule;
import org.springframework.amqp.rabbit.junit.BrokerRunning;

public class TestBase {

    @ClassRule
    public static BrokerRunning brokerRunning = BrokerRunning.isRunningWithEmptyQueues("episodic-progress");

    @AfterClass
    public static void tearDown() {
        brokerRunning.removeTestQueues();
    }

}