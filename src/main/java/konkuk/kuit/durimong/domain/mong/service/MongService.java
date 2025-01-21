package konkuk.kuit.durimong.domain.mong.service;

import konkuk.kuit.durimong.domain.mong.dto.request.MongCreateReq;
import konkuk.kuit.durimong.domain.mong.dto.request.MongNameReq;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MongService {
    private final MongRepository mongRepository;

    public String validateName(MongNameReq req) {
        if (req.getMongName().length() > 6) {
            throw new CustomException(ErrorCode.MONG_NAME_LENGTH);
        }
        return "두리몽의 이름이 " + req.getMongName() + "으로 설정되었습니다.";
    }

    public String createMong(MongCreateReq req) {
        Mong mong = Mong.create(req.getMongName(),req.getImage(),req.getColor());
        mongRepository.save(mong);
        return "두리몽 생성이 완료되었습니다.";
    }
}
