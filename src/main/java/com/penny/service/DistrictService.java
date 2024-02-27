package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictVoMapper districtVoMapper;

    @Autowired
    public DistrictService(DistrictVoMapper districtVoMapper){
        this.districtVoMapper = districtVoMapper;
    }

    public List<DistrictVo> getDistrictsByKeyword(String keyword){
        List<DistrictVo> result = districtVoMapper.selectByNameKeyword(keyword);
        if (result.isEmpty()) throw new ResourceNotFoundException("district with name [%s] not found".formatted(keyword));

        return result;
    }

}
