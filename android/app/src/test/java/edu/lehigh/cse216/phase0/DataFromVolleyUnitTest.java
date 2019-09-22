package edu.lehigh.cse216.phase0;

import android.os.Message;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataFromVolleyUnitTest {
    @Test
    public void messageInfo_constructor_test() {
        MessageInfo data1 = new MessageInfo(1, "Alex", "hello", 5, 2);
        assertEquals(1, data1.msgNum());
        assertEquals("Alex", data1.sender());
        assertEquals("hello", data1.msg());
        assertEquals(5, data1.upvotes());
        assertEquals(2, data1.downvotes());

        MessageInfo data2 = new MessageInfo("John", "poop");
        assertEquals(-1, data2.msgNum());
        assertEquals("John", data2.sender());
        assertEquals("poop", data2.msg());
        assertEquals(0, data2.upvotes());
        assertEquals(0, data2.downvotes());
    }

    @Test
    public void message_info_methods_test() {
        MessageInfo data1 = new MessageInfo(1, "Alex", "hello", 5, 2);

        data1.addUpvote();
        assertEquals(6, data1.upvotes());
        data1.remUpvote();
        assertEquals(5, data1.upvotes());

        data1.addDownvote();
        assertEquals(3, data1.downvotes());
        data1.remDownvote();
        assertEquals(2, data1.downvotes());

        data1.msgNum(100);
        assertEquals(100, data1.msgNum());
    }
}