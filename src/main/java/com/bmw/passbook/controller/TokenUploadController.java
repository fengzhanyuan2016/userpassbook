package com.bmw.passbook.controller;

import com.bmw.passbook.constant.Constants;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenUploadController {
    private final StringRedisTemplate _redisTemplate;

    public TokenUploadController(StringRedisTemplate redisTemplate){
        _redisTemplate = redisTemplate;
    }


    @PostMapping("/token")
    public String tokenFileUpload(@RequestParam String merchantId, @RequestParam String passTemplateId, @RequestParam MultipartFile file){
        if(null == passTemplateId || file.isEmpty()){
            return "passTemplateId is null or file is empty";
        }
        try{
            File current = new File(Constants.TOKEN_DIR+merchantId);
            if(!current.exists()){
                current.mkdir();
            }
            Path path = Paths.get(Constants.TOKEN_DIR,merchantId,passTemplateId);
            Files.write(path,file.getBytes());
            if(!writeTokenToRedis(path,passTemplateId)){
                return "success";
            }
            return "write token error";
        }
        catch (IOException ex){
            ex.printStackTrace();
            return "操作文件失败";
        }
    }



    private boolean writeTokenToRedis(Path path,String key){
        Set<String> tokens;
        try(Stream<String> stream = Files.lines(path)){
            tokens = stream.collect(Collectors.toSet());
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
        if(!CollectionUtils.isEmpty(tokens)) {
            _redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String token : tokens) {
                    connection.sAdd(key.getBytes(), token.getBytes());
                }
                return true;
            });
            return true;
        }
        return false;
    }
}
