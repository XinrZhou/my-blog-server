package myblogserver.controller;

import myblogserver.entity.User;
import myblogserver.exception.XException;
import myblogserver.service.UserService;
import myblogserver.utils.JwtUtil;
import myblogserver.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    Mono<ResultVO> login (@RequestBody User user, ServerHttpResponse response) {
        return userService.getUser(user.getNumber())
                .filter(u -> passwordEncoder.matches(u.getPassword(), user.getPassword()))
                .map(u -> {
                    String jwt = JwtUtil.createJWT(Map.of("uid", u.getId()));
                    response.getHeaders().add("token", jwt);

                    return ResultVO.success();
                })
                .defaultIfEmpty(ResultVO.error(ResultVO.UNAUTHORIZED, "用户名密码不匹配！"));
    }
}
