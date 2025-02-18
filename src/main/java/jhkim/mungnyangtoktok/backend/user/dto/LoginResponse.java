package jhkim.mungnyangtoktok.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String sessionToken; // 세션 토큰 (ex: JWT 또는 세션 ID)
}