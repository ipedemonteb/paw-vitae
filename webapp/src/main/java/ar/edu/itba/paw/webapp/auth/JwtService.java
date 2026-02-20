package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


public class JwtService {

    private final Key key;

    public JwtService(Resource jwtKeyRes) throws IOException {
        key = Keys.hmacShaKeyFor(FileCopyUtils.copyToString(new InputStreamReader(jwtKeyRes.getInputStream())).getBytes(StandardCharsets.UTF_8));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    String ROLE_CLAIM = "role";
    String NAME_CLAIM = "name";
    String SELF_URL_CLAIM = "selfUrl";
    String USER_ID_CLAIM = "userId";




    private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30;
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 10;
    private final String TOKEN_TYPE_CLAIM = "tokenType";

    public String generateToken(ServletUriComponentsBuilder uriBuilder, User user, JwtTokenType type, long duration) {

        return Jwts.builder()
                .setClaims(buildClaims(uriBuilder, user, type))
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(ServletUriComponentsBuilder uriBuilder, User user) {
        return generateToken(uriBuilder, user, JwtTokenType.ACCESS_TOKEN, ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(ServletUriComponentsBuilder uriBuilder, User user) {
        return generateToken(uriBuilder, user, JwtTokenType.REFRESH_TOKEN, REFRESH_TOKEN_VALIDITY );
    }

    private Claims buildClaims(ServletUriComponentsBuilder uriBuilder, User user, JwtTokenType type) {
        final Claims claims = Jwts.claims();
        claims.put(TOKEN_TYPE_CLAIM, type.getType());

        if (!type.isRefreshToken()) {
            claims.put(NAME_CLAIM, user.getName());

            boolean isDoctor = user instanceof Doctor;
            String selfPath = isDoctor ? "doctors" : "patients";


            if (isDoctor) {
                claims.put(ROLE_CLAIM, "ROLE_DOCTOR");
            } else {
                claims.put(ROLE_CLAIM, "ROLE_PATIENT");
            }

            final String selfUrl = uriBuilder
                    .path(selfPath)
                    .path(String.valueOf(user.getId()))
                    .build().toString();

            claims.put(SELF_URL_CLAIM, selfUrl);

            claims.put(USER_ID_CLAIM, user.getId());
        }
        return claims;
    }

    public JwtDetails validate(String token, HttpServletRequest request) {
        try {
            final Jws<Claims> parsed = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            final Claims claims = parsed.getBody();

            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
                throw new ExpiredJwtException(parsed.getHeader(), claims, "JWT token expired");
            }

            return new JwtDetails.Builder()
                    .setId(claims.getId())
                    .setToken(token)
                    .setEmail(claims.getSubject())
                    .setIssuedDate(claims.getIssuedAt())
                    .setExpirationDate(claims.getExpiration())
                    .setTokenType(JwtTokenType.fromCode((claims.get(TOKEN_TYPE_CLAIM).toString())))
                    .build();

        } catch (ExpiredJwtException ex) {
            LOGGER.warn("Expired JWT token - {}", ex.getMessage());
            request.setAttribute("jwt_error", "invalid_token");
            request.setAttribute("jwt_error_desc", "The access token expired");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            LOGGER.warn("Invalid JWT token - {}", ex.getMessage());
            request.setAttribute("jwt_error", "invalid_token");
            request.setAttribute("jwt_error_desc", "Token is invalid or malformed");
        }
        return null;
    }


}
