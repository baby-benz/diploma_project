package com.itmo.accounting.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@UtilityClass
public class OwnerOrgRequestUtil {
    public void notifyOwner(Long equipmentId, boolean toSubscribe) {
        HttpRequest request;

        try {
             request = HttpRequest.newBuilder(
                    new URI("http://localhost:8181/owner/api/mqtt/equipment/" + equipmentId + "?toSubscribe=" + toSubscribe))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e) {
            log.error("Неправильный URL-адрес указан для RESTful API сервиса владельца.");
            return;
        }

        HttpResponse<String> response;

        try {
            response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error("Ошибка при попытке отправить запрос к RESTful API сервису владельца.");
            return;
        } catch (InterruptedException e) {
            return;
        }

        log.info("Сервис потоковой обработки данных ответил: \"{}\"", response.body());
    }
}
