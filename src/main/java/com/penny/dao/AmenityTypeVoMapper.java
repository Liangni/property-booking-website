
package com.penny.dao;

import com.penny.vo.AmenityTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AmenityTypeVoMapper {
    /**
     * 列出所有設施類型。
     *
     * @return 設施類型列表
     */
    List<AmenityTypeVo> listAll();
}
