package com.email.emailwriter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private  final WebClient webClient;
   @Value("${gemini.api.url}")
   private  String geminiApiUrl;

    @Value("${gemini.api.key}")
    private  String geminiApiKey;
   private  String getGeminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public  String generateEmailReply(EmailRequest emailRequest)
    {

        String prompt=buildPrompt(emailRequest);

        Map<String,Object> requestbody=Map.of(
             "contents",new Object[]
                        {
                                Map.of("parts",new Object[]{
                                    Map.of("text",prompt)
                        })
                        }
        );
     String response=webClient.post()
             .uri(geminiApiUrl+geminiApiKey)
             .header("Content-Type","application/json")
             .bodyValue(requestbody)
             .retrieve()
             .bodyToMono(String.class)
             .block();
             
      return  extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
       try{
           ObjectMapper mapper=new ObjectMapper();
           JsonNode rootNode=mapper.readTree(response);
           return  rootNode.path("candidates")
                   .get(0)
                   .path("content")
                   .path("parts")
                   .get(0)
                   .path("text")
                   .asText();
       }
       catch (Exception e)
       {
            return "Error processing request: "+ e.getMessage();
       }
    }

    private String buildPrompt(EmailRequest emailRequest) {
       StringBuilder promt=new StringBuilder();
       promt.append("Generate the professional reply for given content");
       if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty())
       {
            promt.append("Use a").append(emailRequest.getTone()).append("tone");
       }
       promt.append("\nOriginal email: \n").append((emailRequest.getEmailContent()));
        return  promt.toString();


    }
}
