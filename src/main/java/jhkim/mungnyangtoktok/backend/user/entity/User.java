package jhkim.mungnyangtoktok.backend.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String nickname;

    private boolean isAdmin;
    private boolean isPetsitter;
    private boolean isUser;

    public void setAdmin(boolean isAdmin) {
        if (isAdmin) {
            this.isPetsitter = false;
            this.isUser = false;
        }
        this.isAdmin = isAdmin;
    }

    public void setPetsitter(boolean isPetsitter) {
        if (this.isAdmin) {
            throw new IllegalArgumentException("ADMIN은 다른 역할을 가질 수 없습니다.");
        }
        this.isPetsitter = isPetsitter;
    }

    public void setUser(boolean isUser) {
        if (this.isAdmin) {
            throw new IllegalArgumentException("ADMIN은 다른 역할을 가질 수 없습니다.");
        }
        this.isUser = isUser;
    }
}
