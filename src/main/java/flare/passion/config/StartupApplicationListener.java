package flare.passion.config;

import flare.passion.service.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.profiles.active}")
    private String environment;
    @Autowired
    private HistoricalDataService historicalDataService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (environment.equals("prod")) {
            System.out.println("\t\t\t\t[Production] Server Online :)");
            try {
                historicalDataService.saveHistoricalDataToLocal();
                historicalDataService.saveHistoricalDataByWeek();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\t\t\t\t[Development] Server Online :D");
        }
    }

}
