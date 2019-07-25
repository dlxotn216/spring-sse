package demo.app.alert.interfaces.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by itaesu on 24/07/2019.
 */
@Controller
public class AlertViewController {

    @GetMapping(value = "/alerts/{receiver}", produces = MediaType.TEXT_HTML_VALUE)
    public String getAlertView(@PathVariable("receiver") String receiver, ModelMap modelMap) {
        modelMap.addAttribute("receiver", receiver);
        return "alerts/index";
    }

    @GetMapping(value = "/redis/alerts/{receiver}", produces = MediaType.TEXT_HTML_VALUE)
    public String getRedisAlertView(@PathVariable("receiver") String receiver, ModelMap modelMap) {
        modelMap.addAttribute("receiver", receiver);
        return "alerts/redis_index";
    }

    @GetMapping(value = "/redis/alerts/publish")
    public String push(ModelMap modelMap) {
        return "alerts/redis_publish";
    }
}
