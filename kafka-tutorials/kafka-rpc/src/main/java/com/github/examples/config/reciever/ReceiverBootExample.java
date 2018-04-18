package com.github.examples.config.reciever;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;

/**
 */
@Service
@Slf4j
public class ReceiverBootExample {


    @Autowired
    private ReplyingKafkaTemplate<String, Object, Object> template;

    private final Container<UUID, Car> carContainer = new Container<>(5000L);

    public Car sendAndReceiveExample(Car car) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("rpc.request", car);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, "rpc.response".getBytes());
        record.headers().add(KafkaHeaders.CORRELATION_ID, car.getId().toString().getBytes());
        RequestReplyFuture<String, Object, Object> replyFuture = template.sendAndReceive(record);
        template.setReplyTimeout(100000L);

        ConsumerRecord response = replyFuture.get();
//        No correlationId found in reply always timeout exception
        log.info("response: {}", response);
        return (Car) response.value();
    }

    public Car sendAndReceiveExampleSecond(Car car) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("rpc.request2", car);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, "rpc.response".getBytes());
        record.headers().add(KafkaHeaders.CORRELATION_ID, car.getId().toString().getBytes());
        template.send(record);

//        No correlationId found in reply always timeout exception
        return carContainer.get(car.getId());
    }


    public Car sendAndReceiveExampleSecond(Car car,long timeOut) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("rpc.request2", car);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, "rpc.response".getBytes());
        record.headers().add(KafkaHeaders.CORRELATION_ID, car.getId().toString().getBytes());
        template.send(record);
        Thread.sleep(timeOut);
//        No correlationId found in reply always timeout exception
        return carContainer.get(car.getId());
    }

    @KafkaListener(topics = "rpc.response2", groupId = "rpc", containerFactory = "kafkaListenerContainerFactory")
    //    send to doesn't work instead can't find correlation id
    public void example(Message<Car> car) {
        log.info("message from response: {}", car.toString());
        carContainer.addResponse(car.getPayload().getId(), car.getPayload());
    }

    //    simple sync
    private class Container<K, V> {
        private final ConcurrentHashMap<K, Timer<V>> map = new ConcurrentHashMap<>();
        private final Long timeOut;
        private final ScheduledExecutorService scheduledExecutorService;

        public Container(long timeOut) {
            this.timeOut = timeOut;
            this.scheduledExecutorService =new ScheduledThreadPoolExecutor(1);
            scheduledExecutorService.schedule(new CleanerDaemon(),timeOut*3,TimeUnit.MILLISECONDS);
        }

        public void addResponse(K key, V value) {
            map.put(key, new Timer<>(System.currentTimeMillis()+timeOut, value));
        }

        public V get(K key) throws TimeoutException {
            Long beforeException = System.currentTimeMillis() + timeOut;
            while (System.currentTimeMillis() < beforeException) {
                Timer<V> value = map.get(key);
                if (Objects.isNull(value)) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new TimeoutException("can't achieve current response ");
                    }
                } else {
                    map.remove(value);
                    return value.getValue();
                }

            }
            throw new TimeoutException("can't achieve current response ");
        }

        private class Timer<V> {
            private Long timeOutTime;
            private V value;

            public Timer(Long timeOutTime, V value) {
                this.timeOutTime = timeOutTime;
                this.value = value;
            }

            public Long getTimeOutTime() {
                return timeOutTime;
            }

            public V getValue() {
                return value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Timer<?> timer = (Timer<?>) o;

                if (!timeOutTime.equals(timer.timeOutTime)) return false;
                return value.equals(timer.value);

            }

            @Override
            public int hashCode() {
                int result = timeOutTime.hashCode();
                result = 31 * result + value.hashCode();
                return result;
            }

            @Override
            public String toString() {
                return "Timer{" +
                        "timeOutTime=" + timeOutTime +
                        ", value=" + value +
                        '}';
            }
        }

        private class CleanerDaemon implements Runnable {
            @Override
            public void run() {
                map.forEach((k, vTimer) -> {
                    if (vTimer.getTimeOutTime() < System.currentTimeMillis() + timeOut) {
                        map.remove(k);
                        log.info("map cleaned: {} ",vTimer.toString());
                    }
                });
            }
        }
    }
}
