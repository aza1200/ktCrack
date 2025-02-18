package jhkim.mungnyangtoktok.backend.admin.service;

import jhkim.mungnyangtoktok.backend.admin.dto.CodeDetailDto;
import jhkim.mungnyangtoktok.backend.admin.dto.CodeGroupDto;
import jhkim.mungnyangtoktok.backend.admin.entity.CodeDetail;
import jhkim.mungnyangtoktok.backend.admin.entity.CodeGroup;
import jhkim.mungnyangtoktok.backend.admin.repository.CodeDetailRepository;
import jhkim.mungnyangtoktok.backend.admin.repository.CodeGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CodeService {
    private final CodeGroupRepository codeGroupRepository;
    private final CodeDetailRepository codeDetailRepository;

    public CodeService(CodeGroupRepository codeGroupRepository, CodeDetailRepository codeDetailRepository) {
        this.codeGroupRepository = codeGroupRepository;
        this.codeDetailRepository = codeDetailRepository;
    }

    public List<CodeGroup> getAllCodeGroups() {
        return codeGroupRepository.findAll();
    }

    public List<CodeDetail> getAllCodeDetails() {
        return codeDetailRepository.findAll();
    }

    public CodeGroup saveCodeGroup(CodeGroupDto codeGroupDto) {
        CodeGroup codeGroup = new CodeGroup();
        codeGroup.setGroupName(codeGroupDto.getGroupName());
        codeGroup.setDescription(codeGroupDto.getDescription());

        return codeGroupRepository.save(codeGroup);
    }

    // 특정 groupId에 해당하는 CodeDetail 조회 메서드 추가
    public List<CodeDetail> getCodeDetailsByGroupId(Long groupIdString) {
        Long groupId = Long.valueOf(groupIdString); // String → Long 변환
        return codeDetailRepository.findByCodeGroup_GroupId(groupId);
    }

    // 특정 groupId에 해당하는 CodeGroup을 가져옴 (CodeDetails 포함)
    public Optional<CodeGroup> getCodeGroupById(Long groupId) {
        return codeGroupRepository.findById(groupId);
    }

    public boolean deleteCodeGroup(Long groupId) {
        Optional<CodeGroup> codeGroup = codeGroupRepository.findById(groupId);
        if (codeGroup.isPresent()) {
            // 먼저 해당 CodeGroup과 연결된 CodeDetail 삭제
            codeDetailRepository.deleteByGroupId(groupId);
            // CodeGroup 삭제
            codeGroupRepository.deleteById(groupId);
            return true;
        }
        return false;
    }

    public CodeDetail saveCodeDetail(CodeDetailDto codeDetailDto) {
        Optional<CodeGroup> codeGroupOptional = codeGroupRepository.findById(codeDetailDto.getGroupId());

        if (codeGroupOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 groupId를 찾을 수 없습니다: " + codeDetailDto.getGroupId());
        }

        CodeDetail codeDetail = new CodeDetail();
        codeDetail.setCodeGroup(codeGroupOptional.get());
        codeDetail.setCodeName(codeDetailDto.getCodeName());
        codeDetail.setCodeValue(codeDetailDto.getCodeValue());
        codeDetail.setSortOrder(codeDetailDto.getSortOrder());
        codeDetail.setIsActive(codeDetailDto.getIsActive());

        return codeDetailRepository.save(codeDetail);
    }

    // CodeDetail 삭제 메서드 추가
    public boolean deleteCodeDetail(Long codeDetailId) {
        if (codeDetailRepository.existsById(codeDetailId)) {
            codeDetailRepository.deleteById(codeDetailId);
            return true;
        }
        return false;
    }

}