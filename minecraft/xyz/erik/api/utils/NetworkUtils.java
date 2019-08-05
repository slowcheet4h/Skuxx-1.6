package xyz.erik.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.entity.player.EntityPlayer;

import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.RenderEvent;

import java.net.UnknownHostException;

public class NetworkUtils {

    private OldServerPinger pinger = new OldServerPinger();
    private Minecraft mc = Minecraft.getMinecraft();
    private ErikTimer timer = new ErikTimer();
    private long ping;
    private long lastTime;
    private int prevDebugFPS;
    public long updatedPing;

    public NetworkUtils()
    {
        EventManager.register(this);

        PingThread pingThread = new PingThread();
        pingThread.start();
    }

    private class PingThread
            extends Thread
    {
        private PingThread() {}

        public void run()
        {
            for (;;)
            {
                if (NetworkUtils.this.mc.getCurrentServerData() != null) {
                    try
                    {
                        if (!(NetworkUtils.this.mc.currentScreen instanceof GuiScreenServerList)) {
                            NetworkUtils.this.pinger.ping(NetworkUtils.this.mc.getCurrentServerData());
                        }
                    }
                    catch (UnknownHostException localUnknownHostException) {}
                }
                try
                {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getPing()
    {
        return this.ping;
    }

    public int getPlayerPing(String name)
    {
        EntityPlayer player = this.mc.theWorld.getPlayerEntityByName(name);
        if ((player instanceof EntityOtherPlayerMP)) {
            return ((EntityOtherPlayerMP)player).field_175157_a.getResponseTime();
        }
        return 0;
    }

    @EventTarget
    private void on2DRender(RenderEvent event)
    {
        if (Minecraft.debugFPS != this.prevDebugFPS)
        {
            this.prevDebugFPS = Minecraft.debugFPS;
            this.ping = this.updatedPing;
        }
    }
}
