package com.github.examples.config;


import com.github.examples.config.model.Car;
import com.github.examples.config.reciever.ReceiverBootExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KRequestingApplication.class)
public class SpringKafkaJsonApplicationTest {

    @Autowired
    private ReceiverBootExample receiver;


    @Test
    public void testRpc() throws Exception {
        Car response=receiver.sendAndReceiveExampleSecond(new Car("make", "UA", UUID.randomUUID()));

        assertThat(response.getMake()).isEqualTo("new Maker");
    }

    @Test(expected = TimeoutException.class)
    public void testRpcTimeOut() throws Exception {
        receiver.sendAndReceiveExampleSecond(new Car("make", "UA", UUID.randomUUID()),30000L);
    }


}
