package com.warpfuture.iot.api.enterprise.stream.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.iot.api.enterprise.stream.Sender;
import com.warpfuture.iot.api.enterprise.stream.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = Source.class)
public class SenderImpl implements Sender {

    @Autowired
    private Source source;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean send(Object obj) {
        boolean res = false;

        res = source.output().send(MessageBuilder.withPayload(obj).build());

        return res;
    }
}
