package market.feign;

import market.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="market-profiles", url = "http://localhost:8087")
public interface ProfileFeignClient {

    @GetMapping("/api/profiles")
    ProfileDto findProfileByEmail(@RequestParam String email);

    @PostMapping("/api/profiles")
    ProfileDto saveProfile(ProfileDto profile);

    @GetMapping("/api/profile")
    ProfileDto findProfileById(@RequestParam Long accountId);
}
