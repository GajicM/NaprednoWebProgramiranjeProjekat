package rs.raf.demo.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class TokenResponseDto {
    private String token;
    public TokenResponseDto(String token) {
        this.token = token;
    }
}
