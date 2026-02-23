package service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    public static void sendOrderConfirmation(String toEmail, double total) {
        // 1. Thông tin tài khoản gửi
        final String from = "your-email@gmail.com";
        final String pass = "your-app-password";

        // 2. Cấu hình Server SMTP của Gmail
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        // 3. Đăng nhập hệ thống
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try {
            // 4. Tạo nội dung Email
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Xác nhận đơn hàng thành công");

            String formattedTotal = String.format("%,.0f", total);
            msg.setText("Chào bạn,\n\nĐơn hàng của bạn đã được xác nhận thành công.\n"
                    + "Tổng thanh toán: " + formattedTotal + " VNĐ\n\n");

            // 5. Gửi đi
            jakarta.mail.Transport.send(msg);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
