package kr.scramban.wac.parser;

import org.junit.Assert;
import org.junit.Test;

public class InputOrderTest {

    @Test
    public void testVerifyOrderSettingsYour_bot() {
        InputOrder inputOrder = InputOrder.getByOrderKeys("settings", "your_bot", "player1");
        Assert.assertEquals(InputOrder.SETTINGS_YOUR_BOT, inputOrder);
    }
}
