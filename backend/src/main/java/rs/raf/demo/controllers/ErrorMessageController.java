package rs.raf.demo.controllers;

import io.jsonwebtoken.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.services.ErrorMessageService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/errors")
@CrossOrigin
public class ErrorMessageController {

    private final ErrorMessageService errorMessageService;
    private final JwtUtil jwtUtil;
    public ErrorMessageController(ErrorMessageService errorMessageService, JwtUtil jwtUtil) {
        this.errorMessageService = errorMessageService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ErrorMessage>> findAll(@RequestHeader("Authorization") String token) {
       return errorMessageService.findAllbyUserId(jwtUtil.getUserId(token));
    }
}
