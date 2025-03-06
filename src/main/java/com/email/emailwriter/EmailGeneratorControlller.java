package com.email.emailwriter;


import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorControlller {

    private  final  EmailGeneratorService emailGeneratorService;
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest)
    {
         String response=emailGeneratorService.generateEmailReply(emailRequest);
          return  ResponseEntity.ok(response);

    }

}
