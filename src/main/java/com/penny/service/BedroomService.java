package com.penny.service;

import com.penny.dao.BedroomVoMapper;
import com.penny.vo.BedroomVo;
import com.penny.vo.base.BedroomBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BedroomService {

    private final BedroomVoMapper bedroomVoMapper;

    public List<BedroomVo> getPropertyBedroomList(Long propertyId) {
        return bedroomVoMapper.listByPropertyId(propertyId);
    }
}
