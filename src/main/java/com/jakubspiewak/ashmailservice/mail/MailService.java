package com.jakubspiewak.ashmailservice.mail;

import com.jakubspiewak.ashapimodellib.model.mail.ApiReceiveMailQueryParams;
import com.jakubspiewak.ashapimodellib.model.mail.ApiReceiveMailRequest;
import com.jakubspiewak.ashapimodellib.model.mail.ApiReceiveMailResponse;

import java.util.List;

public interface MailService {
    List<ApiReceiveMailResponse> receiveMail(ApiReceiveMailRequest request, ApiReceiveMailQueryParams query);
}
