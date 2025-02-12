package jhkim.mungnyangtoktok.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jhkim.mungnyangtoktok.backend.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void validateUserType() {
        if (!isUser && !isPetsitter) {
            throw new IllegalArgumentException("user, petsitter 중 하나를 반드시 선택해야 합니다.");
        }
    }

    // 비밀번호 암호화 X
    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .isUser(this.isUser)
                .isPetsitter(this.isPetsitter)
                .build();
    }

//    // 비밀번호 암호화
//    public User toEntity(String encodedPassword) {
//        return User.builder()
//                .loginId(this.loginId)
//                .password(encodedPassword)
//                .nickname(this.nickname)
//                .isUser(this.isUser)
//                .isPetsitter(this.isPetsitter)
//                .build();
//    }
}