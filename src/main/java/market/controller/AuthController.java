package market.controller;

import market.dto.AuthResponse;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

import market.model.Message;
import market.service.AuthorizationService;
import market.service.ProducerService;
import market.service.VkAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthorizationService authorizationService;

    private final VkAuthorizationService vkAuthorizationService;

    public AuthController(AuthorizationService authorizationService, VkAuthorizationService vkAuthorizationService) {
        this.authorizationService = authorizationService;
        this.vkAuthorizationService = vkAuthorizationService;
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@RequestBody UserDtoAuthorization user) {
        return authorizationService.signIn(user);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest request) {
        authorizationService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("vk/oauth")
    public void vkAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(vkAuthorizationService.createAuthorizationUrl());
    }

    @GetMapping("vk/sign")
    public AuthResponse vkSigIn(@RequestParam(value = "code") String code) throws IOException, ExecutionException, InterruptedException {
        return authorizationService.authorizationByVk(vkAuthorizationService.authorizationByCode(code));
    }

    @Autowired
    private ProducerService producerService;

    @GetMapping("/generate")
    public String generate(@RequestParam String message, @RequestParam Integer number) {
        producerService.produce(new Message(message, number));
        return "OK";
    }

}
