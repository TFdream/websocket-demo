package websocket.chat.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import websocket.commons.manager.UserAuthManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ricky Fung
 */
@Controller
@RequestMapping("/chat")
public class ChatController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AtomicLong counter = new AtomicLong();
    private final Map<String, Long> userMap = new HashMap<>();

    @GetMapping("/index")
    public ModelAndView hello(@RequestParam("token") String token) {
        ModelAndView mav = new ModelAndView("chat");
        mav.addObject("token", token);
        return mav;
    }

    @GetMapping("/login/{username}")
    public ModelAndView socket(@PathVariable("username") String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        Long userId = userMap.computeIfAbsent(username, (key)-> counter.incrementAndGet());
        LOG.info("在线聊天系统-用户登录开始, username={}, userId={}", username, userId);

        //生成token
        String token = UserAuthManager.getInstance().genToken(userId, username);

        ModelAndView mav = new ModelAndView("/chat");
        mav.addObject("userId", userId);
        mav.addObject("username", username);
        mav.addObject("token", token);
        return mav;
    }
}
