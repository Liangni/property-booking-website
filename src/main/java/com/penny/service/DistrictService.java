package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DistrictService {
    private final DistrictVoMapper districtVoMapper;

    @Autowired
    public DistrictService(DistrictVoMapper districtVoMapper){
        this.districtVoMapper = districtVoMapper;
    }

    public List<DistrictVo> getDistrictsByKeyword(String keyword, int page, int limit){
        int offset = (page - 1) * limit;
        List<DistrictVo> result = districtVoMapper.selectByNameKeyword(keyword, offset, limit);
        if (result.isEmpty()) throw new ResourceNotFoundException("district with name [%s] not found".formatted(keyword));

        return result;
    }

}
