package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.security.CheckPermission;
import rs.raf.demo.services.VacuumService;
import rs.raf.demo.utils.DateUtil;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/vacuums")
public class VacuumController {

    @Autowired
    private final VacuumService vacuumService;
    @Autowired
    private final JwtUtil jwtUtil;

    public VacuumController(VacuumService vacuumService, JwtUtil jwtUtil) {
        this.vacuumService = vacuumService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vacuum> getVacuum(@PathVariable Long id) {
        return ResponseEntity.ok(vacuumService.findById(id).orElse(null));
    }


    @PostMapping("/addVacuum")
    @CheckPermission({Permission.can_add_vacuum})
    public ResponseEntity<Vacuum> addVacuum(@RequestHeader("Authorization") String authorization,@RequestBody String vacuum) {
        return ResponseEntity.ok(vacuumService.save(vacuum,jwtUtil.getUserId(authorization)));
    }

    @DeleteMapping("/deleteVacuum/{id}")
    @CheckPermission({Permission.can_remove_vacuum})
    public ResponseEntity<?> deleteVacuum(@RequestHeader("Authorization") String authorization,@PathVariable Long id) {
        vacuumService.deleteByIdAndUserId(id, jwtUtil.getUserId(authorization));
        return ResponseEntity.ok().build();
    }
    @GetMapping("/searchVacuum")
    @CheckPermission({Permission.can_search_vacuum})
    public ResponseEntity<List<Vacuum>> searchVacuum(@RequestHeader("Authorization") String authorization,
                                                     @RequestParam(required=false) String name,
                                                     @RequestParam(required=false)List<VacuumStatus> status,
                                                     @RequestParam(required=false) String dateFrom,
                                                     @RequestParam(required=false) String dateTo) {

        if(name!=null  && name.isEmpty())name=null;
        if(status!=null && status.isEmpty())status=null;
        return ResponseEntity.ok(vacuumService.search(jwtUtil.getUserId(authorization),
                Optional.ofNullable(name),
                Optional.ofNullable(status),
                Optional.ofNullable(dateFrom),
                Optional.ofNullable(dateTo)));
    }

    @GetMapping("/startVacuum/{id}")
    @CheckPermission({Permission.can_start_vacuum})
    public ResponseEntity<?> startVacuum(@RequestHeader("Authorization") String authorization,@PathVariable Long id) {
        return vacuumService.startVacuum(id,jwtUtil.getUserId(authorization));
    }
    @GetMapping("/stopVacuum/{id}")
    @CheckPermission({Permission.can_stop_vacuum})
    public ResponseEntity<?> stopVacuum(@RequestHeader("Authorization") String authorization,@PathVariable Long id) {
        return vacuumService.stopVacuum(id,jwtUtil.getUserId(authorization));
    }

    @GetMapping("/dischargeVacuum/{id}")
    @CheckPermission({Permission.can_discharge_vacuum})
    public ResponseEntity<?> dischargeVacuum(@RequestHeader("Authorization") String authorization,@PathVariable Long id) {
        return vacuumService.dischargeVacuum(id,jwtUtil.getUserId(authorization));
    }

    @GetMapping("/scheduleStart")
    @CheckPermission({Permission.can_start_vacuum})
    public ResponseEntity<?> scheduleStart(@RequestHeader("Authorization") String authorization, @RequestParam Long id,@RequestParam String date) {
        return vacuumService.scheduleStart(id, DateUtil.convertStringToDate(date),jwtUtil.getUserId(authorization));
    }

    @GetMapping("/scheduleStop")
    @CheckPermission({Permission.can_stop_vacuum})
    public ResponseEntity<?> scheduleStop(@RequestHeader("Authorization") String authorization,@RequestParam Long id,@RequestParam String date) {
        return vacuumService.scheduleStop(id, DateUtil.convertStringToDate(date),jwtUtil.getUserId(authorization));
    }


    @GetMapping("/scheduleDischarge")
    @CheckPermission({Permission.can_discharge_vacuum})
    public ResponseEntity<?> scheduleDischarge(@RequestHeader("Authorization") String authorization,@RequestParam Long id,@RequestParam String date) {

        return vacuumService.scheduleDischarge(id, DateUtil.convertStringToDate(date),jwtUtil.getUserId(authorization));
    }


}
