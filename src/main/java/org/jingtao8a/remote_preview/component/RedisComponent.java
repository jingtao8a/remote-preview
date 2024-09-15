package org.jingtao8a.remote_preview.component;

import org.jingtao8a.remote_preview.constants.Constants;
import org.jingtao8a.remote_preview.dto.DownloadFileDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("redisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public void saveDownloadFileDto(String code, DownloadFileDto downloadFileDto) {
        //5min 后失效
        redisUtils.setex(Constants.REDIS_KEY_DOWNLOAD + code, downloadFileDto, Constants.REDIS_KEY_EXPIRES_FIVE_MIN);
    }

    public DownloadFileDto getDownloadFileDto(String code) {
        return (DownloadFileDto) redisUtils.get(Constants.REDIS_KEY_DOWNLOAD + code);
    }
}
