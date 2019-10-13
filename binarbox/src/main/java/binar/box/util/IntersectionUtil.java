package binar.box.util;

import binar.box.dto.points.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionUtil {
    public static Map<Integer, Pair> bridgeIntersections= new HashMap<>();

    public static List<ValidInterval> validIntervals = populateValidIntervals();

    static
    {
        Integer intersectionSize = 80;

        Integer startY = 551;
        Integer step = 78 ;
        Integer halfStep = 39;
        Integer startXX= 148+halfStep;
        Integer startYY= 551+step;

        for(ValidInterval currentInterval : validIntervals) {
            Integer startX = currentInterval.start;
            for (int x = 0; x < intersectionSize; x++) {
                for (int y = 0; y < 4; y++) {
                    if (x % 2 == 0) {
                        bridgeIntersections.put(x * 4 + y, new Pair(startX + x * step + halfStep, startY + y * step));
                    } else {
                        bridgeIntersections.put(x * 4 + y, new Pair(startXX + x * step, startYY + y * step));
                    }
                }
            }
        }
    }

    //todo:refactorize: put this values in DB
    public static List<ValidInterval> populateValidIntervals()
    {
        List<ValidInterval> validIntervals = new ArrayList<>();
        validIntervals.add(new ValidInterval(149,1007));
        validIntervals.add(new ValidInterval(1127,1985));
        validIntervals.add(new ValidInterval(2103,2964));
        validIntervals.add(new ValidInterval(3079,3938));

        validIntervals.add(new ValidInterval(4056,4917));
        validIntervals.add(new ValidInterval(5035,5896));
        validIntervals.add(new ValidInterval(6012,6872));
        validIntervals.add(new ValidInterval(6993,7853));

        return validIntervals;
    }


    private static class ValidInterval {
        private final int start;
        private final int end;

        public ValidInterval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

}
