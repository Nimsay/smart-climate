package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class AggTest {
    ArrayList<String> a = new ArrayList<>(Arrays.asList("mq","10.2","30.5","mq","mq","32.2","5.2","-2.0","mq"));

    @Test
    void aggMax() {
        assertTrue(Agg.aggMax(a).equals( 32.2));
    }

    @Test
    void aggMin() {
        assertTrue (Agg.aggMin(a).equals(-2.0));
    }

    @Test
    void aggSize() {
        assertTrue (Agg.aggSize(a).equals(5));
    }

    @Test
    void aggMoy() {
        assertEquals( Agg.aggMoy(a),15.22, 0.001);
    }

    @Test
    void aggET() {
        assertEquals(Agg.aggET(a), 13.7399272196, 0.001);
    }

}