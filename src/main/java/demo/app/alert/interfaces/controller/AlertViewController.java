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

    @GetMapping(value="/alerts/{userId}", produces = MediaType.TEXT_HTML_VALUE)
    public String getAlertView(@PathVariable("userId") String userId, ModelMap modelMap){
        modelMap.addAttribute("userId",userId);
        return "alerts/index";
    }

    @GetMapping(value="/redis/alerts/{userId}", produces = MediaType.TEXT_HTML_VALUE)
    public String getRedisAlertView(@PathVariable("userId") String userId, ModelMap modelMap){
        modelMap.addAttribute("userId",userId);
        return "alerts/redis_index";
    }
}
