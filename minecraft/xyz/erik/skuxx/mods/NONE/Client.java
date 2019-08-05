package xyz.erik.skuxx.mods.NONE;

import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.mod.Mod;

public class Client
extends Mod{

    public Bool fastTake = new Bool("fastTake",false);
    
    public Client() {
        addSet(fastTake);
    }

}
