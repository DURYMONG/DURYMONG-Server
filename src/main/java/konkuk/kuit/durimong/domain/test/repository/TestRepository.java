package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.chatbot.dto.response.TestListDto;
import konkuk.kuit.durimong.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("SELECT new konkuk.kuit.durimong.domain.chatbot.dto.response.TestListDto(t.testId, t.name) FROM Test t")
    List<TestListDto> findTestIdAndName();

}
