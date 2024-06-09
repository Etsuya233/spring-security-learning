package com.dc.mapper;

import com.dc.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Etsuya
 * @since 2024-06-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
