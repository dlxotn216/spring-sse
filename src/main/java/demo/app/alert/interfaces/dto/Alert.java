package demo.app.alert.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by itaesu on 24/07/2019.
 */
@Getter @NoArgsConstructor @AllArgsConstructor
public class Alert {
    private String sender;
    private String message;
}
