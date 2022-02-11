package market.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "authorization-service", url = "http://localhost:8082")
public interface AuthFeignClient {
}
