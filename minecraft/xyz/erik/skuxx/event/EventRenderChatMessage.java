/*
 * Created by Erik!
 */

package xyz.erik.skuxx.event;
public class EventRenderChatMessage
extends Event{
    private String message;

    public EventRenderChatMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
