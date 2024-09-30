package com.Rede_Social.Controller.AI;

import com.Rede_Social.Service.AI.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

}
