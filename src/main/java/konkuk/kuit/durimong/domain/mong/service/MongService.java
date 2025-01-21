package konkuk.kuit.durimong.domain.mong.service;

import konkuk.kuit.durimong.domain.mong.dto.request.MongCreateReq;
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

    public String validateName(MongCreateReq req) {
        if (req.getMongName().length() > 6) {
            throw new CustomException(ErrorCode.MONG_NAME_LENGTH);
        }
        return "두리몽의 이름이 " + req.getMongName() + "으로 설정되었습니다.";
    }

}
