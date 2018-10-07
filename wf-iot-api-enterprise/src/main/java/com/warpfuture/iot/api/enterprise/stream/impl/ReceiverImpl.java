package com.warpfuture.iot.api.enterprise.stream.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.iot.api.enterprise.stream.Receiver;
import com.warpfuture.iot.api.enterprise.stream.Sink;
import com.warpfuture.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableBinding(value = Sink.class)
public class ReceiverImpl implements Receiver {

    @Autowired
    private ObjectMapper mapper;

    @Override
    @StreamListener(value = Sink.INPUT)
    public void receive(String msg) {
        try {
            mapper.readValue(msg, ResultVO.class);
        } catch (IOException e) {

        }
    }

}
