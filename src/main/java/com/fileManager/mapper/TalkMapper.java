package com.fileManager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fileManager.entity.Logs;
import com.fileManager.entity.Talk;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Mapper
public interface TalkMapper extends BaseMapper<Talk> {

}
