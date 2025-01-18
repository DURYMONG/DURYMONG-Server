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
        if (password.length() < 8 || password.length() > 16) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!password.matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }



        if (!password.matches(".*[a-z].*")) {
            throw new CustomException(USER_PASSWORD_LOWER);
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new CustomException(USER_PASSWORD_UPPER);
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new CustomException(USER_PASSWORD_SPECIAL);
        }
        return null;
    }

}
