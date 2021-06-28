package com.jakubspiewak.ashmailservice.mail;

import com.jakubspiewak.ashapimodellib.model.mail.ApiReceiveMailQueryParams;
import com.jakubspiewak.ashapimodellib.model.mail.ApiReceiveMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/receive")
    public ResponseEntity<?> getMails(
            @RequestBody(required = false) ApiReceiveMailRequest request,
            ApiReceiveMailQueryParams query
    ) {
        final var response = mailService.receiveMail(request, query);

        return ResponseEntity
                .status(OK)
                .body(response);
    }
}