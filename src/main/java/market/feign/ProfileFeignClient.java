package market.feign;



import market.dto.ProfileDto;
import market.hystrix.ProfileClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "MARKET-PROFILES", fallbackFactory = ProfileClientFallbackFactory.class)
public interface ProfileFeignClient {

    @GetMapping("/api/profiles/email")
    ProfileDto findProfileByEmail(@RequestParam String email);

    @PostMapping("/api/profiles")
    ProfileDto saveProfile(ProfileDto profile);

    @GetMapping("/api/profiles/{id}")
    ProfileDto findProfileById(@PathVariable Long id);

}
