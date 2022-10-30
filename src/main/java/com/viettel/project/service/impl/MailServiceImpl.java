package com.viettel.project.service.impl;

import com.viettel.project.entity.Email;
import com.viettel.project.repository.MailRepository;
import com.viettel.project.service.MailService;
import com.viettel.project.service.dto.MailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Value(value = "${spring.mail.username}")
    public String fromm;

    @Override
    public void sendMail(MailDTO mailDTO){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

            String from = mailDTO.getFromMail() == null ? fromm : mailDTO.getFromMail();
            helper.setFrom(from);
            helper.setTo(mailDTO.getToMail());
            helper.setSubject(mailDTO.getSubject());
            String cc= mailDTO.getCc();
            if(!Objects.isNull(cc) && !cc.isEmpty()){
                helper.setCc(cc);
            }

            Context context = new Context();
            context.setVariable("name", mailDTO.getToName());
            context.setVariable("content", mailDTO.getContent());
            String html = springTemplateEngine.process("email-form", context);
            helper.setText(html, true);

            // send mail content.
            javaMailSender.send(mimeMessage);

            // save mail to database
            Email email = new Email();
            BeanUtils.copyProperties(mailDTO, email);
            email.setResult("S");
            mailRepository.save(email);
        }catch(Exception e){
            Email email = new Email();
            BeanUtils.copyProperties(mailDTO, email);
            email.setResult("F");
            mailRepository.save(email);
            e.printStackTrace();
            return;
        }

        logger.info("Send mail to " + mailDTO.getToName() + " successfully!");
    }

    @Override
    public List<MailDTO> getAllMail() {
        return mailRepository.findAll().stream()
                .map(mail -> {
                    MailDTO mailDTO = new MailDTO();
                    BeanUtils.copyProperties(mail, mailDTO);
                    return mailDTO;
                })
                .collect(Collectors.toList());
    }
}
