package org.pcp.tournament.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class PcpErrorController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    @RequestMapping("/error")
    protected String error(final RedirectAttributes redirectAttributes) throws IOException {
        logger.error("Handling error");
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:/login";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}