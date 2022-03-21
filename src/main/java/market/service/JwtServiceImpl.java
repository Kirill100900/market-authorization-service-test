package market.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import market.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@PropertySource("classpath:jwt.properties")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.token.expiration}")
    private long jwtExpiration;

    @Value("${jwt.token.secret}")
    private String secret;

    @Override
    public String createToken(Account account) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(account.getEmail())
                .claim("role", account.getRole().getAuthority())
                .claim("account_id", account.getId())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
