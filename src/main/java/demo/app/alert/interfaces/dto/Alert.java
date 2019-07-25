package demo.app.alert.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Created by itaesu on 24/07/2019.
 */
@Getter @NoArgsConstructor @AllArgsConstructor
public class Alert {
    private static final String ANONYMOUS = "Anonymous";

    private String sender;
    private String receiver;
    private String message;

    public Alert(String receiver, String message) {
        this(ANONYMOUS, receiver, message);
    }

    public String getSender(){
        return StringUtils.isEmpty(this.sender)
                ? ANONYMOUS
                : this.sender;
    }
}
