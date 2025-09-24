package com.medelin.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
{
    public String HomeControllerHandler()
    {
        return "user service controller";
    }
}
