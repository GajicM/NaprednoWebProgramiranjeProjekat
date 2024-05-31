package rs.raf.demo.controllers;

import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rs.raf.demo.model.Permission;
import rs.raf.demo.security.CheckPermission;
import rs.raf.demo.utils.JwtUtil;

@RestController
@RequestMapping("/teller")
@CrossOrigin
public class SSEController {

        JwtUtil jwtUtil;
        public SSEController(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }
        private final Logger logger = LoggerFactory.getLogger(SSEController.class);


        @GetMapping("/subscribe/{token}")
        public SseEmitter streamSse(@PathVariable String token) {
       long userId=Long.parseLong(token);
            SseEmitter emitter = new SseEmitter();
            logger.info("Emitter created with timeout {} for subscriberId {}", emitter.getTimeout(), userId);
            SseEmitterManager.addEmitter(token, emitter);

            // Set a timeout for the SSE connection (optional)
            emitter.onTimeout(() -> {
                logger.info("Emitter timed out");
                emitter.complete();
                SseEmitterManager.removeEmitter(token);
            });

            // Set a handler for client disconnect (optional)
            emitter.onCompletion(() -> {
                logger.info("Emitter completed");
                SseEmitterManager.removeEmitter(token);
            });

            return emitter;
        }
    }



