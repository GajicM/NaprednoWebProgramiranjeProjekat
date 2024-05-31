package rs.raf.demo.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;


import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements IAuthService {

    @Value("${oauth.jwt.secret}")
    private static  String secret="abcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcdefabcde";
    private static final Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }
    @Override
    public String generateJWT(User user) {
        // JWT-om mozete bezbedono poslati informacije na FE
        // Tako sto sve sto zelite da posaljete zapakujete u claims mapu
        // Claims se koristi da sacuvamo vise podataka o nosiocu tokena
        // Informacije o njemu, koje privilegije ima i slicno
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("subject", user.getEmail());
        claims.put("permissions",user.getPermissions());
        System.out.println(user);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 60L))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }


    public boolean isAuthorized(String token) {
        System.out.println("token: "+token);
        if (!isEmpty(token) ) {
         //   String jwt = token.substring(token.indexOf("Bearer ") + 7);
           // System.out.println("jwt: "+jwt);
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        //    System.out.println(claims.getPayload().getSubject());
          //  System.out.println("email: "+claims.getPayload().get("email"));
           // System.out.println(userRepository.findUserByEmail((String) claims.getPayload().get("email")));
            if (userRepository.findUserByEmail((String) claims.getBody().get("email")) != null) {
                return true;
            }
        }
        return false;
    }

    public Claims parseToken(String jwt) throws Exception{ //THROWS EXPIRED JWT EXCEPTION
        Jws<Claims> claims = null;

         claims   = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);

        return claims.getBody();
    }
    private static boolean isEmpty(String s){
        return s == null || s.isEmpty();
    }

}

