package Utils.Seeders;

import models.Company;
import models.DbModels.DeliveryArea;
import models.DbModels.DeliveryJob;
import models.DbModels.DeliveryService;
import models.DbModels.Staff;

import java.util.ArrayList;
import java.util.List;

public class DeliveryJobSeeder {
    public static List<DeliveryJob> deliveryJobs(List<DeliveryService> deliveryServices, List<Staff> staffs, List<DeliveryArea> areas){
        List<DeliveryJob> jobs = new ArrayList<>();

        DeliveryJob job = new DeliveryJob();
        job.setDeliveryService(deliveryServices.get(1));
        job.setStaff(staffs.get(1));
        job.setDeliveryArea(areas.get(1));
        job.setCompleted(true);
        job.setAssignedOn(Company.getTodaysDate());
        jobs.add(job);
        
        return jobs;
    }
}
