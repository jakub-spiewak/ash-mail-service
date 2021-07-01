package com.jakubspiewak.ashmailservice.mail;

import com.jakubspiewak.ashapimodellib.model.mail.ApiFetchMailRequest;
import com.jakubspiewak.ashapimodellib.model.mail.ApiFetchMailResponse;

import java.util.List;

public interface MailService {
    List<ApiFetchMailResponse> fetchMail(ApiFetchMailRequest request);
}
