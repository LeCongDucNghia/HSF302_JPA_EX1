package com.de200635.mvc.service.impl;

import com.de200635.mvc.dto.DeviceDTO;
import com.de200635.mvc.entity.Category;
import com.de200635.mvc.entity.Device;
import com.de200635.mvc.repository.CategoryRepository;
import com.de200635.mvc.repository.DeviceRepository;
import com.de200635.mvc.service.DeviceService;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final CategoryRepository categoryRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DeviceServiceImpl(
            DeviceRepository deviceRepository,
            CategoryRepository categoryRepository) {

        this.deviceRepository = deviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAllByOrderByInstallationDateDesc();
    }

    @Override
    public List<Device> searchByName(String keyword) {
        return deviceRepository
                .findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(keyword);
    }

    @Override
    public Device getById(Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public void validateBusinessRules(
            DeviceDTO deviceDTO,
            BindingResult bindingResult) {

        // Check duplicate Device Code
        if (deviceDTO.getDeviceCode() != null
                && !deviceDTO.getDeviceCode().isBlank()) {

            if (deviceRepository.findByDeviceCode(
                    deviceDTO.getDeviceCode().trim()).isPresent()) {

                bindingResult.addError(new FieldError(
                        "DeviceDTO",
                        "deviceCode",
                        "Device Code already exists"
                ));
            }
        }

        // Check Category exists
        if (deviceDTO.getCategoryId() != null) {

            if (!categoryRepository.existsById(
                    deviceDTO.getCategoryId())) {

                bindingResult.addError(new FieldError(
                        "DeviceDTO",
                        "categoryId",
                        "Selected category does not exist"
                ));
            }
        }

        // Check Installation Date
        if (deviceDTO.getInstallationDate() != null
                && !deviceDTO.getInstallationDate().isBlank()) {

            try {
                LocalDate installationDate = LocalDate.parse(
                        deviceDTO.getInstallationDate(),
                        DATE_FORMAT
                );

                if (installationDate.isAfter(LocalDate.now())) {
                    bindingResult.addError(new FieldError(
                            "DeviceDTO",
                            "installationDate",
                            "Installation date must not be in the future"
                    ));
                }

            } catch (DateTimeParseException e) {
                bindingResult.addError(new FieldError(
                        "DeviceDTO",
                        "installationDate",
                        "Invalid date format. Please use dd/MM/yyyy"
                ));
            }
        }
    }

    @Override
    public void createDevice(DeviceDTO deviceDTO) {

        Category category = categoryRepository
                .findById(deviceDTO.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Category not found")
                );

        Device device = new Device();

        device.setDeviceCode(deviceDTO.getDeviceCode().trim());
        device.setDeviceName(deviceDTO.getDeviceName().trim());
        device.setCategory(category);
        device.setPrice(deviceDTO.getPrice());
        device.setWarrantyMonths(deviceDTO.getWarrantyMonths());

        device.setInstallationDate(
                LocalDate.parse(
                        deviceDTO.getInstallationDate(),
                        DATE_FORMAT
                )
        );

        device.setStatus(deviceDTO.getStatus());

        deviceRepository.save(device);
    }

    @Override
    public void deleteDevice(Integer id) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Device not found with ID: " + id
                        )
                );

        // Cannot delete Active device
        if ("AC".equalsIgnoreCase(device.getStatus())) {
            throw new IllegalStateException(
                    "Cannot delete an active device"
            );
        }

        deviceRepository.delete(device);
    }
}
