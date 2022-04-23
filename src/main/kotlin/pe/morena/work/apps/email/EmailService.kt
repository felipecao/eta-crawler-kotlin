package pe.morena.work.apps.email

import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Component
import javax.mail.internet.MimeMessage

@Component
class EmailService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun sendEmail(email: Email) {
        val prep = MimeMessagePreparator { mimeMessage: MimeMessage? ->
            val message = MimeMessageHelper(mimeMessage!!)
            message.setTo(email.to)
            message.setFrom(email.from, "Felipe Carvalho")
            message.setReplyTo(email.from, "Felipe Carvalho")
            message.setSubject(email.subject)
            message.setText(email.contents, false)
        }

        try {
            buildSender().send(prep);
            logger.info("Email with ETAs successfully sent to ${email.to}")
        }
        catch (e: Exception) {
            logger.error("Exception when trying to send e-mail: ${e.message}", e)
        }
    }

    private fun buildSender() : JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        val props = mailSender.javaMailProperties

        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = System.getenv("GMAIL_USER")
        mailSender.password = System.getenv("GMAIL_PWD")

        props.put("mail.transport.protocol", "smtp")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.debug", "false")

        return mailSender
    }
}