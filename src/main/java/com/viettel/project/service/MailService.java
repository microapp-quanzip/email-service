package com.viettel.project.service;

import com.viettel.project.service.dto.MailDTO;

import java.util.List;

public interface MailService {
    void sendMail(MailDTO mailDTO);
    List<MailDTO> getAllMail();
}
