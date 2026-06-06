package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service này chuyên xử lý các tác vụ bảo trì hệ thống định kỳ.
 * Việc tách riêng ra giúp FineService không bị quá tải và tập trung vào nghiệp vụ phí phạt.
 */
@Service
@RequiredArgsConstructor
public class LibraryMaintenanceService {

    private final FineService fineService;

    // Chạy vào 1:00 AM mỗi ngày
    // Cron format: giây, phút, giờ, ngày trong tháng, tháng, thứ trong tuần
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0 * * * * ?") 
    @Transactional
    public void runAutoTasks() {
        System.out.println("Đang chạy tác vụ bảo trì...");
        fineService.processOverdueLocks();
    }
}