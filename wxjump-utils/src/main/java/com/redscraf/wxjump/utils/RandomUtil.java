package com.redscraf.wxjump.utils;

import java.util.Random;

/**
 * <p>function:
 * <p>User: LeeJohn
 * <p>Date: 2018/1/4
 * <p>Version: 1.0
 */
public class RandomUtil {
    private static Random random = new Random();

    /**
     * @param max
     * @return [1,max+1)
     */
    public static int random(int max){
        int result = random.nextInt(max);
        return result + 1;
    }
}
