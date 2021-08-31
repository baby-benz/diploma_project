package com.itmo.owner.controller;

import com.itmo.owner.service.MQTTManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/owner/api/")
@RequiredArgsConstructor
public class SubscriptionController {
    private final MQTTManager MQTTManager;

    @PostMapping("/mqtt/equipment/{equipmentId}")
    public String subscribeOrUnsubscribe(@PathVariable Long equipmentId, @RequestParam boolean toSubscribe) throws NoSuchFieldException, ContractException, IOException, InterruptedException, TimeoutException {
        return MQTTManager.subscribeOrUnsubscribe(equipmentId, toSubscribe);
    }
}
