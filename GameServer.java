package com.nightshift.tv;

import android.content.Context;
import fi.iki.elonen.NanoHTTPD;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Map;

public class GameServer extends NanoHTTPD {
    private final Context ctx;
    private volatile String tvState = "{\"ev\":0,\"got\":0,\"state\":\"play\"}";
    private volatile double px = 0.5, py = 0.5;
    private volatile String pg = "0";
    private volatile long pt = 0;

    public GameServer(Context c, int port) {
        super(port);
        ctx = c;
    }

    @Override
    public Response serve(IHTTPSession s) {
        String u = s.getUri();
        Map<String, String> q = s.getParms();
        try {
            if (u.equals("/tv")) return asset("tv.html");
            if (u.equals("/") || u.equals("/index.html")) return asset("pad.html");
            if (u.equals("/info")) {
                return json("{\"url\":\"http://" + ip() + ":" + getListeningPort() + "\"}");
            }
            if (u.equals("/sync")) {
                tvState = "{\"ev\":" + num(q.get("ev")) + ",\"got\":" + num(q.get("got"))
                        + ",\"state\":\"" + ("win".equals(q.get("state")) ? "win" : "play") + "\"}";
                return json("{\"x\":" + px + ",\"y\":" + py + ",\"g\":" + pg
                        + ",\"age\":" + (System.currentTimeMillis() - pt) + "}");
            }
            if (u.equals("/pad")) {
                px = clamp(q.get("x"));
                py = clamp(q.get("y"));
                pg = num(q.get("g"));
                pt = System.currentTimeMillis();
                return json(tvState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not found");
    }

    private Response asset(String name) throws IOException {
        InputStream in = ctx.getAssets().open(name);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
        in.close();
        Response r = newFixedLengthResponse(Response.Status.OK, "text/html; charset=utf-8", bo.toString("UTF-8"));
        r.addHeader("Cache-Control", "no-store");
        return r;
    }

    private Response json(String s) {
        Response r = newFixedLengthResponse(Response.Status.OK, "application/json", s);
        r.addHeader("Cache-Control", "no-store");
        return r;
    }

    private static String num(String v) {
        try { return String.valueOf(Integer.parseInt(v)); } catch (Exception e) { return "0"; }
    }

    private static double clamp(String v) {
        try {
            double d = Double.parseDouble(v);
            return d < 0 ? 0 : (d > 1 ? 1 : d);
        } catch (Exception e) {
            return 0.5;
        }
    }

    private static String ip() {
        try {
            for (NetworkInterface n : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!n.isUp() || n.isLoopback()) continue;
                for (InetAddress a : Collections.list(n.getInetAddresses())) {
                    if (a instanceof Inet4Address && a.isSiteLocalAddress()) return a.getHostAddress();
                }
            }
        } catch (Exception e) { /* ignore */ }
        return "your-tv-ip";
    }
}
