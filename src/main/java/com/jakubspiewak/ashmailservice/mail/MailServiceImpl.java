package com.jakubspiewak.ashmailservice.mail;

import com.jakubspiewak.ashapimodellib.model.mail.*;
import com.jakubspiewak.ashapimodellib.model.util.DateRange;
import jodd.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static jodd.mail.EmailFilter.Operator.*;
import static jodd.mail.EmailFilter.filter;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private static final String INBOX_FOLDER_NAME = "INBOX";

    @Override
    public List<ApiFetchMailResponse> fetchMail(final ApiFetchMailRequest request) {
        final var session = getImapServer(request.getConfiguration()).createSession();

        session.open();
        session.useFolder(INBOX_FOLDER_NAME);

        final var filter = createMailsFilter(request.getQuery());
        final var receivedEmailStream = Stream.of(session.receiveEmail(filter));

        session.close();

        return receivedEmailStream
                .map(MailServiceImpl::mapReceivedMailToResponse)
                .collect(toList());
    }

    private EmailFilter createMailsFilter(MailQueryParams query) {
        final var filter = createDefaultFilter();
        final var fromFilter = filter().or();

        // TODO: should be better approach
        ofNullable(query.getFrom()).ifPresent(froms -> {
            froms.forEach(fromFilter::from);
            if (!froms.isEmpty()) filter.and(fromFilter);
        });

        final var maxDate = getOptionalDate(query, DateRange::getMax);
        maxDate.ifPresent(date -> filter.and().sentDate(LE, date.getTime()));

        final var minDate = getOptionalDate(query, DateRange::getMin);
        minDate.ifPresent(date -> filter.and().sentDate(GE, date.getTime()));

        return filter;
    }

    private EmailFilter createDefaultFilter() {
        return filter().and().sentDate(LT, currentTimeMillis());
    }

    private static ApiFetchMailResponse mapReceivedMailToResponse(ReceivedEmail source) {
        return ApiFetchMailResponse.builder()
                .from(source.from().getEmail())
                .subject(source.subject())
                .receiptDate(source.sentDate())
                .attachments(source.attachments()
                        .stream()
                        .map(MailServiceImpl::mapEmailAttachmentsToResponseType)
                        .collect(toList())
                )
                .build();
    }

    private static MailAttachment mapEmailAttachmentsToResponseType(EmailAttachment<?> source) {
        return MailAttachment.builder()
                .name(source.getName())
                .content(source.toByteArray())
                .build();
    }

    private static Optional<Date> getOptionalDate(MailQueryParams query, Function<DateRange, Date> mapper) {
        return ofNullable(query.getDate()).map(mapper);
    }

    private static ImapServer getImapServer(MailConfiguration config) {
        return MailServer.create()
                .host(config.getHost())
                .port(config.getPort())
                .ssl(true)
                .auth(config.getMailAddress(), config.getPassword())
                .buildImapMailServer();
    }
}

