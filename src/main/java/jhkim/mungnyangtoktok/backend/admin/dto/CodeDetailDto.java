package jhkim.mungnyangtoktok.backend.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDetailDto {
    private Long groupId;
    private String codeName;
    private String codeValue;
    private Integer sortOrder;
    private Boolean isActive;
}
