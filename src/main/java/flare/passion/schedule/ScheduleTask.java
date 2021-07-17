package flare.passion.schedule;

import flare.passion.service.HistoricalDataService;
import flare.passion.service.NewsService;
import flare.passion.service.RumorService;
import flare.passion.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private HistoricalDataService historicalDataService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private RumorService rumorService;
    @Autowired
    private TransportService transportService;

    @Scheduled(cron = "0 4 0 * * ?")
    private void GetHistoricalData() {
        try {
            historicalDataService.saveHistoricalDataToLocal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    private void GetNews() {
        try {
            newsService.getNewsFromQQNews();
        } catch (Exception e) {
            System.out.println("get news task failed");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void GetRumors() {
        try {
            rumorService.getRumorsFromWebsiteBelow();
        } catch (Exception e) {
            System.out.println("get rumors task failed");
        }
    }

    @Scheduled(cron = "0 5 0 * * ?")
    private void SaveHistoricalDataByWeek() {
        try {
            historicalDataService.saveHistoricalDataByWeek();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void GetAnnotations() {
        try {
            newsService.getAnnouncementFromDxy();
        } catch (Exception e) {
            System.out.println("get annotations task failed");
        }
    }


    @Scheduled(cron = "0 0 0 * * ?")
    private void saveFlight() {
        try {
            transportService.saveFlightInfo();
            transportService.saveTrainInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
