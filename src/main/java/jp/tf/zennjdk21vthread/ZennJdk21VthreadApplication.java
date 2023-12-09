package jp.tf.zennjdk21vthread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class ZennJdk21VthreadApplication {
    private static final Logger log = LoggerFactory.getLogger(ZennJdk21VthreadApplication.class);

    public static void main(String[] args) {
        log.info("!!! started from ZennJdk21VthreadApplication...");
    }
}
