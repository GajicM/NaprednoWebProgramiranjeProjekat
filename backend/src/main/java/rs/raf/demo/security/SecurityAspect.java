package rs.raf.demo.security;


import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.demo.authentication.AuthService;
import rs.raf.demo.model.Permission;
import rs.raf.demo.utils.JwtUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private JwtUtil jwtUtil;

    public SecurityAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Around("@annotation(rs.raf.demo.security.CheckPermission)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(Arrays.asList(methodSignature.getParameterNames()));
        //Check for authorization parameter
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            System.out.println(methodSignature.getParameterNames()[i]);
            if (methodSignature.getParameterNames()[i].equalsIgnoreCase("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        //If token is not presents return UNAUTHORIZED response


        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //Try to parse token
        Claims claims = null;
        try{
            claims= jwtUtil.parseToken(token);
            System.out.println(claims);
            //If fails return UNAUTHORIZED response
            if (claims == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if(jwtUtil.isTokenExpired(token)){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("Token has expired",HttpStatus.UNAUTHORIZED);
        }


        //Check user role and proceed if user has appropriate role for specified route
        CheckPermission checkSecurity = method.getAnnotation(CheckPermission.class);
        List<String> permissions = claims.get("permissions", List.class);
        System.out.println(claims.get("permissions", List.class));
        System.out.println(permissions.get(0));

      for(String p: permissions) {
        Permission px=  Enum.valueOf(Permission.class, p);
          System.out.println(px);
          if (Arrays.asList(checkSecurity.value()).contains(px)) {
              return joinPoint.proceed();
          }
      }
   /*     if (Arrays.asList(checkSecurity.value()).contains(permission)) {
            return joinPoint.proceed();
        }*/
        //Return FORBIDDEN if user hasn't appropriate role for specified route
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}