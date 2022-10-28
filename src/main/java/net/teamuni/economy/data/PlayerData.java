package net.teamuni.economy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerData {
    private final String uuid;
    private long money;
    public void afterDeposit(long depositedMoney) {
        this.money = depositedMoney;
    }
    public void afterWithdraw(long withDrewMoney) {
        this.money = withDrewMoney;
    }
}
