package flare.passion.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import flare.passion.model.User;
import flare.passion.repository.UserRepository;
import flare.passion.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@Component
public class AuthChecker implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        boolean granted = false;
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        if (token != null) {
            String email;
            try {
                email = JWT.decode(token).getAudience().get(0);
                Optional<User> optionalUser = userRepository.findById(email);
                if (optionalUser.isEmpty()) {
                    return false;
                }
                User user = optionalUser.get();
                boolean verified = jwtTokenUtil.verifyToken(token, user);
                if (verified) {
                    granted = true;
                }
            } catch (JWTDecodeException ignored) {

            }
        }
        if (granted) {
            return true;
        } else {
            JSONObject ret = new JSONObject();
            ret.put("message", "unauthorized request");
            PrintWriter out = response.getWriter();
            out.write(ret.toString());
            out.flush();
            out.close();
            return false;
        }
    }

}
