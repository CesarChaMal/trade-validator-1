package priv.rdo.trade.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {
    @RequestMapping(value = "/documentation")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
