package demo.app.event.interfaces;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by itaesu on 24/07/2019.
 */
@Controller
public class EventViewController {
    @GetMapping(value = "/events", produces = MediaType.TEXT_HTML_VALUE)
    public String getTemperatureView() {
        return "event/index";
    }
}
