package com.springSec.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secapi/v1/country")
public class CountryController {
    @PostMapping("/addCountry")
    public String addCountry(){
        return "added";
    }
}
