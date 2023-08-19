package com.eardh.gateway.center.mapper.api;

import com.eardh.gateway.center.model.api.ApiRateLimiter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * API限流表Mapper
* @author eardh
*/
@Mapper
public interface ApiRateLimiterMapper extends BaseMapper<ApiRateLimiter> {

}




