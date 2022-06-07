package market.service;

import market.dto.AuthVkUser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface VkAuthorizationService {
    public String createAuthorizationUrl();

    public AuthVkUser authorizationByCode(String code) throws IOException, ExecutionException, InterruptedException;
}
