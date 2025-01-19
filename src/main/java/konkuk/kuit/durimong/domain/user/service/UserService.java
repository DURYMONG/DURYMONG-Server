package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String validateId(String id) {
        if(userRepository.existsById(id)) {
            throw new CustomException(USER_DUPLICATE_ID);
        }
        return null;
    }
    public String validateEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(USER_DUPLICATE_EMAIL);
        }
        return null;
    }
    public String validatePassword(String password) {
        if (password.length() < 6 || password.length() > 10) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!password.matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            throw new CustomException(USER_PASSWORD_ENGLISH);
        }

        return null;
    }

}
