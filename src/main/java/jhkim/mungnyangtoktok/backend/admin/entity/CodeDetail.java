package jhkim.mungnyangtoktok.backend.admin.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "code_detail")
public class CodeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정
    private Long id; // 기존의 codeId 대신 id를 PKey로 변경

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore  // << 추가된 부분 >>
    private CodeGroup codeGroup;

    @Column(length = 100, nullable = false)
    private String codeName;

    @Column(length = 50, nullable = false)
    private String codeValue;

    @Column(nullable = false)
    private Integer sortOrder = 1;

    @Column(nullable = false)
    private Boolean isActive = true;
}
