package binar.box.util;

import binar.box.dto.points.Pair;

import java.util.HashMap;
import java.util.Map;

public class IntersectionUtil {
    public static Map<Integer, Pair> bridgeIntersections= new HashMap<>();

    static
    {
        Integer intersectionSize = 192;
        Integer startX = 148;
        Integer startY = 551;
        Integer step = 78 ;
        Integer halfStep = 39;
        Integer startXX= 148+halfStep;
        Integer startYY= 551+step;



        for (int x=0; x < intersectionSize; x++) {
            for (int y=0; y < 4;y++) {
                if (x % 2 == 0) {
                    bridgeIntersections.put(x*4+y, new Pair(startX + x*step + halfStep, startY + y*step));
                } else {
                    bridgeIntersections.put(x*4+y, new Pair(startXX + x*step, startYY + y*step));
                }
            }
        }
    }
}
