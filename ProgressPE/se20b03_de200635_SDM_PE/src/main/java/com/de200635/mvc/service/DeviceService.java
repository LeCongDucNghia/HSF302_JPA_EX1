package com.de200635.mvc.service;

import com.de200635.mvc.dto.DeviceDTO;
import com.de200635.mvc.entity.Device;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface DeviceService {

    // Q1.1 - List devices ordered by Installation Date descending
    List<Device> getAllDevices();

    // Q1.2 - Search by device name
    List<Device> searchByName(String keyword);

    // Q4 - View device details
    Device getById(Integer id);

    // Q3.2 - Business rule validation
    void validateBusinessRules(
            DeviceDTO deviceDTO,
            BindingResult bindingResult
    );

    // Q3.2.a - Create new device
    void createDevice(DeviceDTO deviceDTO);

    // Q2.2 - Delete device
    void deleteDevice(Integer id);
}
