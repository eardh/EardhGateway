package com.eardh.gateway.starter.registry;

import com.alibaba.nacos.api.exception.NacosException;

/**
 * @author eardh
 * @date 2023/4/5 12:44
 */
public interface RegistryService {

    void initRegistry() throws NacosException;

    void registerInstance() throws NacosException;

}
