package team5.profile.model.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendSMS(String to, String text) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
            new com.twilio.type.PhoneNumber(to),
            new com.twilio.type.PhoneNumber(fromNumber),
            text
        ).create();
        System.out.println("SMS sent to " + to);
    }
}
