package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String validateId(String id) {
        if(userRepository.existsById(id)) {
            throw new CustomException(ErrorCode.USER_DUPLICATE_ID);
        }
        return null;
    }
    public String validateEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_DUPLICATE_EMAIL);
        }
        return null;
    }
}
