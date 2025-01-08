package konkuk.kuit.durimong.domain.auth;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Operation(summary = "테스트1" , description = "테스트1")
    @GetMapping("test1")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("");
    }

    @Operation(summary = "테스트2" , description = "테스트2")
    @GetMapping("test2")
    public ResponseEntity<String> test2(@RequestParam String name){
        return ResponseEntity.ok(name);
    }
}
