package rs.raf.demo.controllers;

import lombok.var;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SseEmitterManager {
    private static final Logger logger = Logger.getLogger(SseEmitterManager.class.getName());
    private static final Map<String, SseEmitter> emitters = new HashMap<>();

    public static void addEmitter(String subscriberId, SseEmitter emitter) {
        emitters.put(subscriberId, emitter);
    }

    public static void removeEmitter(String subscriberId) {
        emitters.remove(subscriberId);
    }

    public static void sendSseEventToClients(String subscriberId, VacuumStatus data) {
        SseEmitter emitter = emitters.get(subscriberId);
        System.out.println(data+ "VOVVOVOVOVO");

        if (emitter == null) {
            logger.warning("No client with subscriber Id " + subscriberId + " found!");
            return;
        }
        try {
            emitter.send(data);

        } catch (IOException e) {
            logger.warning("Error sending event to client: " + e.getMessage());
        }
    }
}
