package jhkim.mungnyangtoktok.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @NotNull(message = "user, petsitter 중 하나를 선택해주세요.")
    private Boolean isUser;

    @NotNull(message = "user, petsitter 중 하나를 선택해주세요.")
    private Boolean isPetsitter;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .isUser(this.isUser)
                .isPetsitter(this.isPetsitter)
                .build();
    }
}
