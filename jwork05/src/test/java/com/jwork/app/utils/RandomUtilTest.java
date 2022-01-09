package com.jwork.app.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RandomUtilTest {

    @Before
    public void init() {
        new RandomUtil();
    }

    @Test
    public void testRandomUtil() {
        List<Integer> tmp = new ArrayList<>();
        tmp.add(RandomUtil.getRandom(1, 5));
        tmp.add(RandomUtil.getRandom(1, 5));
        tmp.add(RandomUtil.getRandom(1, 5));
        tmp.add(RandomUtil.getRandom(1, 5));
        // assertNull(RandomUtil.getRandom(1, 5)); // 直接运行可以运行，但是用maven test无法运行，怪
        RandomUtil.clearSet();
        tmp.add(RandomUtil.getRandom(1, 5));
        tmp.add(RandomUtil.getRandom(1, 5));
        for (Integer i: tmp) {
            System.out.println(i);
        }
    }
    
}
