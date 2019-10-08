package com.example.demo.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestTask {
    private static final Logger logger = LogManager.getLogger(TestTask.class);

    public void run() {
        logger.info("定時器運行了!!!");
    }
}
