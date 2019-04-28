package dev.alangomes.springspigot.event;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class EventService {

    @Autowired
    private EventScanner eventScanner;

    @Autowired
    private SpringEventExecutor eventExecutor;

    @Autowired
    private Server server;

    @Autowired
    private Plugin plugin;

    public void registerEvents(Listener listener) {
        eventScanner.getListenerMethods(listener).forEach(method -> registerEvents(listener, method));
    }

    private void registerEvents(Listener listener, Method method) {
        EventHandler handler = method.getAnnotation(EventHandler.class);
        Class<? extends Event> eventType = (Class<? extends Event>) method.getParameters()[0].getType();
        server.getPluginManager().registerEvent(eventType, listener, handler.priority(), eventExecutor, plugin, handler.ignoreCancelled());
    }

}