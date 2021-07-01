package com.jakubspiewak.ashmailservice.mail;

import com.jakubspiewak.ashapimodellib.model.mail.ApiFetchMailRequest;
import com.jakubspiewak.ashapimodellib.model.mail.ApiFetchMailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/fetch")
    public ResponseEntity<List<ApiFetchMailResponse>> getMails(@RequestBody ApiFetchMailRequest request) {
        final var response = mailService.fetchMail(request);

        return ResponseEntity
                .status(OK)
                .body(response);
    }
}