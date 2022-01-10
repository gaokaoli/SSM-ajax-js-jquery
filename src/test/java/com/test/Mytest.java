package com.test;

import com.manongyanjiuseng.utils.MD5Util;
import org.junit.Test;


public class Mytest {
    @Test
    public void testD5(){
        String mi= MD5Util.getMD5("000000");
        System.out.println(mi);
    }
}
