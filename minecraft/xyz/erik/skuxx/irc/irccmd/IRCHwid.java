package xyz.erik.skuxx.irc.irccmd;

import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.irc.Myself;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class IRCHwid
extends IRCCommand{




    public IRCHwid() {
        super("hwid");
    }


    public void run(String text) {

        try {
            String hwid = Myself.getHWID();

            Skuxx.getInstance().getMyself().sendMessage("@! " + hwid);

        }catch (NoSuchAlgorithmException | UnsupportedEncodingException a){
            Skuxx.getInstance().getMyself().sendMessage("@!Error on sending hwid");
        }


    }
}
