package jhkim.mungnyangtoktok.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String loginId;
    private String nickName;

    private boolean isUser;
    private boolean isAdmin;
    private boolean isPetsitter;

    private String sessionId; // 세션 ID 추가
}
