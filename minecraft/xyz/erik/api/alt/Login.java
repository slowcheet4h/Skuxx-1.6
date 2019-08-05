package xyz.erik.api.alt;


import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;


import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import xyz.erik.skuxx.Skuxx;

public class Login extends Thread
{
    private final Minecraft mc;
    private final String password;
    private String status;
    private final String username;

    public Login(final String username, final String password) {
        super("§6Alt Manager");
        this.mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        this.status = "§7Waiting for login...";
    }

    private final Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException e) {
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = "§aLogged in. (" + this.username + "§a - offline name)";

            Skuxx.getInstance().updateSkuxxPlayer();
            return;
        }
        this.status = "§eLogging in...";
        final Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = "§cLogin failed!";
        }
        else {
            this.status = "§aLogined. (" + auth.getUsername() + "§a)";
            this.mc.session = auth;
            Skuxx.getInstance().updateSkuxxPlayer();
        }
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
