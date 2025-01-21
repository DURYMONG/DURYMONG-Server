package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static konkuk.kuit.durimong.global.exception.ErrorCode.UNABLE_TO_SEND_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            log.info("Sending email to {}", toEmail);
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.error("Failed to send email: {}", e.getMessage(), e);
            throw new CustomException(UNABLE_TO_SEND_EMAIL);
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
