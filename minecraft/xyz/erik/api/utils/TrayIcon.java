package xyz.erik.api.utils;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class TrayIcon {


    public static void addNotification(String title,String text) {
        try {
            if (SystemTray.isSupported()) {
                TrayIcon td = new TrayIcon();
                td.displayTray(title,text);
            } else {
            }
        }catch (AWTException E){}catch (MalformedURLException a){}
    }
    public static void displayTray(String title,String text) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(text, title, MessageType.INFO);
    }
}
