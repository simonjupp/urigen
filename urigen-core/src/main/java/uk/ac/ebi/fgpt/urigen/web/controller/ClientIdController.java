package uk.ac.ebi.fgpt.urigen.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Simon Jupp
 * @date 22/12/2016
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
@Controller
@RequestMapping("/clientId")
public class ClientIdController {

    public ClientIdController() {
    }

    @Value("${urigen.github.clientId}")
    public String clientId;

    @RequestMapping(method = RequestMethod.GET)
        public @ResponseBody
    String getClientId() {
        return clientId;
    }
}
