package com.penny.service;

import com.penny.dao.AddressVoMapper;
import com.penny.dao.DistrictVoMapper;
import com.penny.dao.base.AddressBaseVoMapper;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceDuplicateException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.CreateAddressRequest;
import com.penny.vo.AddressVo;
import com.penny.vo.base.AddressBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
     * @throws RequestValidationException 如果 districtId 或 street 為 null，或者 district 不是行政劃分層級 3 的行政區
     * @throws ResourceDuplicateException 如果該地址已存在
     */
    public void createAddress(CreateAddressRequest request) {
        Long districtId = request.getDistrictId();
        String street = request.getStreet();

        // 限制 district 只能是行政劃分層級 3 的行政區
        if (districtVoMapper.selectAdminAreaLevelByPrimaryKey(districtId) != 3L) {
            throw new RequestValidationException("only district at administrative area level 3 is valid. The given district is at level %s".formatted(districtId));
        }

        // 檢查該地址是否已存在
        AddressVo existingAddress = addressVoMapper.selectByDistrictIdAndStreet(districtId, street);
        if(existingAddress != null) {
            throw new ResourceDuplicateException("address already exists");
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

    /**
     * 根據地址 ID 獲取地址基本資訊。
     *
     * @param addressId 地址的 ID
     * @return 返回地址基本資訊
     * @throws ResourceNotFoundException 如果找不到指定 ID 的地址，則拋出資源未找到異常
     */
    public AddressBaseVo getAddress(Long addressId) {
        return Optional.ofNullable(addressVoMapper.selectWithDistrictByAddressId(addressId))
                .orElseThrow(() -> new ResourceNotFoundException("address with id %s not found".formatted(addressId)));
    }
}
