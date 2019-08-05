package xyz.erik.api.notification;

import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventRender;
import xyz.erik.skuxx.event.events.EventTick;
import xyz.erik.skuxx.event.events.RenderEvent;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static List<Notification> notificationList = new ArrayList<>();

    public NotificationManager() {
        EventManager.register(this);
    }


    @EventTarget
    private void onTick(EventTick eventTick) {
      for(int i = 0; i < notificationList.size(); i++) {
          Notification ntf = notificationList.get(i);
          if (ntf.time > 0) {
              ntf.time--;
          } else {
              notificationList.remove(i);
          }
      }
    }

    @EventTarget
    public void render(EventRender e) {
        int index = 0;
        for(Notification  n : notificationList) {
            if (n.time > 0) {
                int y = index * 10;
                n.draw(e.getHeight() - y,e.getWidth());
            }
            index++;
        }
    }

    public static void addNotification(Notification n) {

        notificationList.add(n);
    }
}
