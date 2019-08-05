package xyz.erik.api.friend;

import xyz.erik.api.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    public List<Friend> friendList = new ArrayList<>();

    private File file;
    public void start() {
        this.file = FileUtils.getConfigFile("friends");
        load();
        save();
    }

    public void load() {
        List<String> strings = FileUtils.read(file);
        friendList.clear();
        for(String str : strings) {
            if (!str.contains("::"))continue;
            String[] split = str.split("::");
            friendList.add(new Friend(split[0],split[1]));
            continue;
        }
    }


    public String replace(String text) {
        for(Friend friend : friendList) {
            text = text.replaceAll(friend.getName(),friend.getAlias());
        }
        return text;
    }

    public void removeFriend(String name) {
        Friend friend =getFriend(name);
        friendList.remove(friend);
    }

    public void addFriend(String name,String alias) {
        if (!isFriend(name)) {
            Friend friend = new Friend(name, alias);
            friendList.add(friend);
            save();
        }
    }
    public void save() {
        List<String> strings = new ArrayList<>();
        for(Friend friend : friendList) {
            strings.add(friend.getName() + "::" + friend.getAlias());
        }

        FileUtils.write(file,strings,true);
    }

    public boolean isFriend(String name) {
        for(Friend fr : friendList) {
            if (fr.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Friend getFriend(String name) {
        for(Friend f : friendList){
            if (f.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()) ||
                    f.getAlias().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
            return f;
        }
        return null;
    }
}
