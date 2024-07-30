package com.example.api_gateway.securityJwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.api_gateway.data.vo.v1.security.TokenVO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey="secret";

    @Value("${security.jwt.token.expire-lenght:360000}")
    private long validityInMilliseconds= 360000; // 1h

    @Autowired
    private UserDetailsService service;

    private Algorithm algorithm = null;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    private String getAccessToken(String userName, List<String> roles, Date now, Date validity) {
    }
    private String getAccessToken(String userName, List<String> roles, Date now) {
    }

    public TokenVO createAccessToken(String userName, List<String> roles){
        Date now = new Date();
        Date validity = new Date(now.getTime()+validityInMilliseconds);
        var accessToken = getAccessToken(userName,roles,now,validity);
        var refreshToken = getAccessToken(userName,roles,now);
        return new TokenVO(userName,true,now,validity,accessToken,refreshToken);
    }


}
