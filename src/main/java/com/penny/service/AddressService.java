package com.penny.service;

import com.penny.dao.AddressVoMapper;
import com.penny.dao.DistrictVoMapper;
import com.penny.dao.base.AddressBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceExistException;
import com.penny.request.CreateAddressRequest;
import com.penny.vo.AddressVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressBaseVoMapper addressBaseVoMapper;

    private final AddressVoMapper addressVoMapper;

    private final DistrictVoMapper districtVoMapper;


    /**
     * 創建新地址。
     *
     * @param request 地址創建請求
     * @throws FieldConflictException 如果 districtId 或 street 為 null，或者 district 不是行政劃分層級 3 的行政區
     * @throws ResourceExistException 如果該地址已存在
     */
    public void createAddress(CreateAddressRequest request) {
        Long districtId = request.getDistrictId();
        String street = request.getStreet();

        // 檢查 districtId 和 street 是否為 null
        if(street == null) throw new FieldConflictException("street is required");

        if (districtId == null) throw new FieldConflictException("districtId is required");

        // 限制 district 只能是行政劃分層級 3 的行政區
        if (districtVoMapper.selectAdminAreaLevelByPrimaryKey(districtId) != 3L) {
            throw new FieldConflictException("only district at administrative area level 3 is valid. The given district is at level %s".formatted(districtId));
        }

        // 檢查該地址是否已存在
        AddressVo existingAddress = addressVoMapper.selectByDistrictIdAndStreet(districtId, street);
        if(existingAddress != null) {
            throw new ResourceExistException("address already exists");
        }

        // 插入新地址
        addressBaseVoMapper.insertSelective(
                AddressVo
                        .builder()
                        .adminAreaLevel3DistrictId(districtId)
                        .street(street)
                        .build()
        );
    }
}
