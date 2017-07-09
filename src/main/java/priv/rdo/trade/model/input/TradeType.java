package priv.rdo.trade.model.input;

import java.util.Arrays;
import java.util.List;

/*
 * was enum but one can't pass enums to annotations, so I decided to go with String
 */
public interface TradeType {
    String SPOT = "Spot";
    String FORWARD = "Forward";
    String VANILLA_OPTION = "VanillaOption";

    static List<String> values() {
        return Arrays.asList(SPOT, FORWARD, VANILLA_OPTION);
    }
}
