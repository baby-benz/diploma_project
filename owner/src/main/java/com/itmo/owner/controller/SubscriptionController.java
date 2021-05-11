package com.itmo.owner.controller;

import com.itmo.owner.service.MQTTManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/owner/api/")
@RequiredArgsConstructor
public class SubscriptionController {
    private final MQTTManager MQTTManager;

    @PostMapping("/mqtt/sensor/{sensorId}")
    public String subscribeOrUnsubscribe(@PathVariable Long sensorId, @RequestParam boolean toSubscribe) throws NoSuchFieldException {
        return MQTTManager.subscribeOrUnsubscribe(sensorId, toSubscribe);
    }

    @PostMapping("/mqtt/hensor")
    public void commission(@RequestBody DDO ddd) {
    }
}
