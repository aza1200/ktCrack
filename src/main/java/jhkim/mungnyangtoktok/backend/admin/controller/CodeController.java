package jhkim.mungnyangtoktok.backend.admin.controller;

import jhkim.mungnyangtoktok.backend.admin.dto.CodeDetailDto;
import jhkim.mungnyangtoktok.backend.admin.dto.CodeGroupDto;
import jhkim.mungnyangtoktok.backend.admin.entity.CodeDetail;
import jhkim.mungnyangtoktok.backend.admin.entity.CodeGroup;
import jhkim.mungnyangtoktok.backend.admin.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/codes")
public class CodeController {
    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/groups")
    public List<CodeGroup> getAllCodeGroups() {
        return codeService.getAllCodeGroups();
    }

    @GetMapping("/details")
    public List<CodeDetail> getAllCodeDetails() {
        return codeService.getAllCodeDetails();
    }

    // 특정 groupId에 해당하는 CodeDetail 조회 API 추가
    @GetMapping("/details/{groupId}")
    public ResponseEntity<List<CodeDetail>> getCodeDetailsByGroupId(@PathVariable Long groupId) {
        List<CodeDetail> codeDetails = codeService.getCodeDetailsByGroupId(groupId);
        return ResponseEntity.ok(codeDetails);
    }

    // CodeGroup 추가하는 API
    @PostMapping("/groups")
    public ResponseEntity<CodeGroup> addCodeGroup(@RequestBody CodeGroupDto codeGroupDto) {
        CodeGroup savedGroup = codeService.saveCodeGroup(codeGroupDto);
        return ResponseEntity.ok(savedGroup);
    }

    // 특정 groupId에 대한 CodeGroup과 관련된 CodeDetail 반환
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> getCodeGroupWithDetails(@PathVariable Long groupId) {
        Optional<CodeGroup> codeGroup = codeService.getCodeGroupById(groupId);
        if (codeGroup.isPresent()) {
            return ResponseEntity.ok(codeGroup.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<?> deleteCodeGroup(@PathVariable Long groupId) {
        boolean isDeleted = codeService.deleteCodeGroup(groupId);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PostMapping("/details")
    public ResponseEntity<CodeDetail> addCodeDetail(@RequestBody CodeDetailDto codeDetailDto) {
        CodeDetail savedDetail = codeService.saveCodeDetail(codeDetailDto);
        return ResponseEntity.ok(savedDetail);
    }

    // CodeDetail 삭제 엔드포인트 추가
    @DeleteMapping("/details/{codeDetailId}")
    public ResponseEntity<?> deleteCodeDetail(@PathVariable Long codeDetailId) {
        boolean isDeleted = codeService.deleteCodeDetail(codeDetailId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //

}