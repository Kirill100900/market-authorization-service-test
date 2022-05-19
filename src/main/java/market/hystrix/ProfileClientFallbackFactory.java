package market.hystrix;

import market.dto.ProfileDto;
import market.exception.AccountSaveException;
import market.feign.ProfileFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
public class ProfileClientFallbackFactory implements FallbackFactory<ProfileFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClient.class);

    @Override
    public ProfileFeignClient create(Throwable cause) {
        return new ProfileFeignClient() {
            @Override
            public ProfileDto findProfileByEmail(String email) {
                LOGGER.error("It worked Hystrix Circuit Breaker");
                throw new RuntimeException();
            }


            @Override
            public ProfileDto saveProfile(ProfileDto profile) {
                LOGGER.error("It worked Hystrix Circuit Breaker");
                try {
                    throw new AccountSaveException("Profile creation error");
                } catch (AccountSaveException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public ProfileDto findProfileById(Long accountId) {
                LOGGER.error("It worked Hystrix Circuit Breaker");
                throw new RuntimeException();
            }

        };
    }

}
