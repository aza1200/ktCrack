package jhkim.mungnyangtoktok.backend.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "code_group")
public class CodeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long groupId; // Long 타입으로 변경

    @Column(length = 100, nullable = false)
    private String groupName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "codeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodeDetail> codeDetails;
}