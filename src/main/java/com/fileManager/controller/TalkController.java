package com.fileManager.controller;

import com.fileManager.entity.Talk;
import com.fileManager.entity.Users;
import com.fileManager.service.ITalkService;
import com.fileManager.service.IUsersService;
import com.fileManager.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/talk")
public class TalkController {

    @Autowired
    private ITalkService talkService;
    @Autowired
    private IUsersService usersService;

    /**
     * 发布评论
     * @param commentData 包含评论内容的 Map
     * @param isSafe 是否启用 XSS 防范（true 为启用，false 为不启用）
     * @return ResponseEntity 提示信息
     */
    @PostMapping("/publish")
    public ResponseEntity<?> publishTalk(@RequestBody Map<String, String> commentData, @RequestParam boolean isSafe) {
        // 获取当前登录用户的 ID
        String userId = UserContext.getCurrentUser();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录，无法发布评论");
        }

        Users userById = usersService.getUserById(userId);
        // 获取评论内容
        String text = commentData.get("text");

        // 如果 isSafe 为 true，则进行转义和过滤
        if (isSafe) {
            text = escapeHtml(text);  // 转义 HTML 特殊字符
        }

        // 创建评论对象并保存
        Talk talk = new Talk();
        talk.setUsername(userById.getUsername());
        talk.setText(text);
        talkService.save(talk);  // 调用 service 层保存评论

        return ResponseEntity.status(HttpStatus.CREATED).body("评论发布成功");
    }

    /**
     * 将评论内容中的 HTML 特殊字符进行转义
     * @param str 要转义的字符串
     * @return 转义后的字符串
     */
    private String escapeHtml(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }
    /**
     * 获取评论列表，按时间倒序排列
     * @return ResponseEntity 评论列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<Talk>> getTalkList() {
        // 获取按时间倒序排列的评论列表
        List<Talk> talks = talkService.lambdaQuery()
                .orderByDesc(Talk::getCreatedAt)
                .list();

        return ResponseEntity.ok(talks); // 返回 200 状态码和评论列表
    }

    /**
     * 删除评论
     * @param id 评论 ID
     * @return ResponseEntity 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTalk(@PathVariable("id") String id) {
        // 根据 ID 删除评论
        boolean removed = talkService.removeById(id);

        if (removed) {
            return ResponseEntity.ok("评论删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("删除评论失败，评论 ID 不存在");
        }
    }
}
