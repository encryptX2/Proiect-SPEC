package proiect.listeners;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class MostRequestsListener implements UpdateListener {

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        EventBean event = newEvents[0];
        String retailer = (String) event.get("retailerName");
        int numberOfProducts = (int) event.get("nrOfProducts");
        System.out.println( retailer + " has requested the most products, "
                + numberOfProducts + ".");
    }

}
