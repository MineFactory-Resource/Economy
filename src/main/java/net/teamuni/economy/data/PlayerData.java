package net.teamuni.economy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerData {
    private final UUID uuid;
    private Map<String, Long> moneyMap;
    public void afterDeposit(String economyID, long depositedMoney) {
        this.moneyMap.put(economyID, depositedMoney);
    }
    public void afterWithdraw(String economyID, long withdrewMoney) {
        this.moneyMap.put(economyID, withdrewMoney);
    }
}
