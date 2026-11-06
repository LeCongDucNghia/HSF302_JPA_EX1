package com.de200635.mvc.repository;

import com.de200635.mvc.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // Q1.1 - Default list ordered by InstallationDate descending
    List<Device> findAllByOrderByInstallationDateDesc();

    // Q1.2 - Search by device name (LIKE %...%), ignore case
    List<Device> findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(String deviceName);

    // Q3 - Check duplicate device code
    Optional<Device> findByDeviceCode(String deviceCode);

    // Check device code exists
    boolean existsByDeviceCode(String deviceCode);
}
