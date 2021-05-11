package com.itmo.accounting.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс, содержащий в себе список сообщений об ошибках.
 */
@Setter
@Getter
@AllArgsConstructor
public class ApiError {
    private List<String> errors;
}

