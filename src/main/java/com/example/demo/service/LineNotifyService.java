/*package com.example.demo.service;



import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class LineNotifyService {
	
	private final WebClient webClient;
	
	public LineNotifyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://notify-api.line.me").build();
    }

    // แก้ไขให้รับ Token เข้ามาด้วย
    public void sendNotification(String message, String token) {
        if (token == null || token.isBlank()) {
            return; // ไม่ต้องทำอะไรถ้าไม่มี Token
        }
        webClient.post()
                .uri("/api/notify")
                .header("Authorization", "Bearer " + token) // ใช้ Token ที่รับมา
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("message=" + message)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }

}*/
