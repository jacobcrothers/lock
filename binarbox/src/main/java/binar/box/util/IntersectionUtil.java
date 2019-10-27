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
        final int X_STEP = 78;
        final int Y_STEP = 155;

        final Integer startUpY = 550;
        final Integer startDownY = 628;
        final Integer endUpY = 863;
        int nrOfIntersections = 0;
        boolean isUp = true;
        for(ValidInterval currentInterval : validIntervals) {
            Integer startX = currentInterval.start;
            while(startX < currentInterval.end) {

                int intiY = isUp? startUpY : startDownY;
                for(int y = intiY; y < endUpY; ) {
                    y= y + Y_STEP;
                    bridgeIntersections.put(nrOfIntersections++, new Pair(startX ,  y ));
                }
                isUp = ! isUp;
                startX += X_STEP;
            }
        }
    }

    //todo:refactorize: put this values in DB
    public static List<ValidInterval> populateValidIntervals()
    {
        List<ValidInterval> validIntervals = new ArrayList<>();
        validIntervals.add(new ValidInterval(147,1007));
        validIntervals.add(new ValidInterval(1127,1989));
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
