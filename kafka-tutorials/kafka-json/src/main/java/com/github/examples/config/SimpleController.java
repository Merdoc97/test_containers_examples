package com.github.examples.config;


import com.github.examples.config.model.Car;
import com.github.examples.config.producer.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by igor on 6/14/18.
 */
@RestController
public class SimpleController {

    @Autowired
    private Sender sender;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void produce(){
        List<Car> carList=Arrays.asList(new Car("make","manufactured","id"));
        sender.send(carList);
    }
}
