package rs.raf.demo.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.repositories.ErrorMessageRepository;

import java.util.List;

@Service
public class ErrorMessageService {
    private final ErrorMessageRepository errorMessageRepository;

    public ErrorMessageService(ErrorMessageRepository errorMessageRepository) {
        this.errorMessageRepository = errorMessageRepository;
    }

    public ResponseEntity<List<ErrorMessage>> findAllbyUserId(Long id) {
       return new ResponseEntity<>(errorMessageRepository.findAllByUserId(id), org.springframework.http.HttpStatus.OK);
    }
}
