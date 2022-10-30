package com.viettel.project.controller;

import com.viettel.project.service.MailService;
import com.viettel.project.service.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class EmailController {

    @Autowired
    private MailService mailService;

    @PreAuthorize(value = "(#oauth2.hasScope('email') || hasRole('ADMIN')) && isAuthenticated()")
    @PostMapping(value = "/send")
    public void sendMain(@RequestBody MailDTO mailDTO) throws MessagingException {
        mailService.sendMail(mailDTO);
    }

    @PreAuthorize(value = "(#oauth2.hasScope('email') || hasRole('ADMIN')) && isAuthenticated()")
    @GetMapping(value = "/emails")
    public ResponseEntity<List<MailDTO>> getAllMail(){
        return ResponseEntity.ok(mailService.getAllMail());
    }
}
