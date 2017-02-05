package proiect;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import proiect.events.ProductRequestEvent;
import proiect.listeners.BigRequestsListener;
import proiect.listeners.MostRequestsListener;

/**
 * Cerinte proiect:
 * 1. Care este retailerul cu cele mai multe produse cerute in ultimele 24 de ore.
 * 2. Pentru a detecta ambuteiaje in zona de ambalari se va monitoriza aparitia a 2 cereri consecutive pentru comenzi 
 * mari (peste 3000 de produse).
 */

public class Main {

    //private static final int SECONDS_PER_HOUR = 60 * 60;
    // Easy switch constant to see results faster
    private static final int SECONDS_PER_HOUR = 1;

    private static final int MILLIS_PER_SEC   = 1000;

    public static void main(String[] args) throws InterruptedException {
        // Config the event system
        Configuration config = new Configuration();
        config.addEventTypeAutoName("proiect.events");
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
        // Launch queries
        // 1. Get the retailer having the most requests in the last 24h
        // Get the requests in the last 24h
        String createRecentReqWindow = "create window recentRequests.win:time(" + (24 * SECONDS_PER_HOUR)
                + " sec) as select * from ProductRequestEvent";
        String insertRecentReq = "insert into recentRequests select * from ProductRequestEvent";
        epService.getEPAdministrator().createEPL(createRecentReqWindow);
        epService.getEPAdministrator().createEPL(insertRecentReq);
        // For each entry in recentRequests, compute the number of products grouped by retailer, select the top one
        String mostReqPerRetailer = "select retailerName, sum(quantity) as nrOfProducts "
                + "from recentRequests group by retailerName output snapshot every "
                + (1 * SECONDS_PER_HOUR) + " seconds order by sum(quantity) desc limit 1";

        EPStatement statement = epService.getEPAdministrator().createEPL(mostReqPerRetailer);
        statement.addListener(new MostRequestsListener());

        // 2. Get sequential events having big orders (over 3000 products together)
        String bigOrderReq = "select (a.quantity + b.quantity) as bigOrderNumber  from pattern[ every a = ProductRequestEvent -> b = ProductRequestEvent] where a.quantity + b.quantity > 3000";

        EPStatement bigOrderStatement = epService.getEPAdministrator().createEPL(bigOrderReq);
        bigOrderStatement.addListener(new BigRequestsListener());
        
        // Register events
        registerEvents(epService);

    }

    private static void registerEvents(EPServiceProvider epService) throws InterruptedException {
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32014, 120));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32015, 300));
        Thread.sleep(2 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32018, 100));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32002, 400));
        Thread.sleep(3 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32099, 200));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32072, 100));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32072, 220));
        Thread.sleep(2 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32071, 1000));
        Thread.sleep(2 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32047, 2200));
        Thread.sleep(2 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32031, 500));
        Thread.sleep(3 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32015, 1800));
        Thread.sleep(4 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32012, 1800));
        Thread.sleep(4 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32011, 210));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32011, 410));
        Thread.sleep(1 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32002, 820));
        Thread.sleep(4 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32071, 340));
        Thread.sleep(2 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32070, 100));
        Thread.sleep(3 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32054, 800));
        Thread.sleep(5 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("MegaImage", 32002, 320));
        Thread.sleep(4 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Carrefour", 32054, 100));
        Thread.sleep(4 * SECONDS_PER_HOUR * MILLIS_PER_SEC);
        epService.getEPRuntime().sendEvent(new ProductRequestEvent("Auchan", 32072, 1200));
    }
}
