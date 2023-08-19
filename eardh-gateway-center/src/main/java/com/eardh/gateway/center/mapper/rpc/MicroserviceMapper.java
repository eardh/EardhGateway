package com.eardh.gateway.center.mapper.rpc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eardh.gateway.center.model.rpc.MicroService;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author eardh
 * @date 2023/3/22 13:52
 */
@Mapper
public interface MicroserviceMapper extends BaseMapper<MicroService> {

}
