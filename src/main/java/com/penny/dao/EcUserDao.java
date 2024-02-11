package com.penny.dao;

import com.penny.vo.EcUserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcUserDao {
    EcUserVo getUserByPk(@Param("userId") Long userId);
}
