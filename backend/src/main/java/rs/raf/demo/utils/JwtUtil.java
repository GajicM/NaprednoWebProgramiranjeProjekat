package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${oauth.jwt.secret}")
    private  String SECRET_KEY ;
    @Autowired
    private UserRepository userRepository;

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {

        return (String) extractAllClaims(token).get("subject");
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

  /*  public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }*/

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }


    public String generateToken(User user) {
        // JWT-om mozete bezbedono poslati informacije na FE
        // Tako sto sve sto zelite da posaljete zapakujete u claims mapu
        // Claims se koristi da sacuvamo vise podataka o nosiocu tokena
        // Informacije o njemu, koje privilegije ima i slicno

        user =userRepository.findUserByEmail(user.getEmail());
        System.out.println(user);
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("subject", user.getEmail());
        claims.put("id",user.getId());
        claims.put("permissions",user.getPermissions());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 60L))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }




    public Claims parseToken(String jwt) throws Exception{ //THROWS EXPIRED JWT EXCEPTION
        Jws<Claims> claims = null;

        claims   = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);

        return claims.getBody();
    }

    public Long getUserId(String authorization){
        String token=authorization.substring(7);
        return extractAllClaims(token).get("id",Long.class);
    }

}
