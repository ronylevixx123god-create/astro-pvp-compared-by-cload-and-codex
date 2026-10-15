package net.crankedpvp.hypeboxes.struct.battle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.crankedpvp.hypeboxes.struct.HypeBoxReward;

import java.util.ArrayList;
import java.util.List;

@Getter @AllArgsConstructor
public class BattleInfo {
    private List<HypeBoxReward> hypeBoxRewardList;

    public BattleInfo() {
        this.hypeBoxRewardList = new ArrayList<>();
    }

    public void addItem(HypeBoxReward hypeBoxReward) {
        this.hypeBoxRewardList.add(hypeBoxReward);
    }

    public double getValue() {
        double value = 0;
        for (HypeBoxReward hypeBoxReward : hypeBoxRewardList) {
            value += hypeBoxReward.getValue();
        }
        return value;
    }
}
