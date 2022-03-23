package market.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import market.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Long expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("account_id", account.getId());
        claims.put("role", account.getRole().getAuthority());
        return doGenerateToken(claims, account.getEmail());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        final Date createdDate = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(new Date((new Date()).getTime() + this.expirationTime))
                .signWith(key)
                .compact();
    }
}
