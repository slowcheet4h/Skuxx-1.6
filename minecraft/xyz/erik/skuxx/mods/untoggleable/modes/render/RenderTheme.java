package xyz.erik.skuxx.mods.untoggleable.modes.render;

import xyz.erik.skuxx.event.events.KeyPressEvent;

public abstract class RenderTheme
{

    public abstract void render(int height, int width);
    public void onStart(){}
    public void onStop() {}
    public abstract void keyPress(KeyPressEvent e);

}
