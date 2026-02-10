package com.app.employeedesk.response;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    public String messageResponse(String key) {
        return messageSource.getMessage(key, null, Locale.ENGLISH);
    }
}