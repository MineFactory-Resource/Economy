package net.teamuni.economy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerData {
    private final UUID uuid;
    private long money;
    public void afterDeposit(long depositedMoney) {
        this.money = depositedMoney;
    }
    public void afterWithdraw(long withDrewMoney) {
        this.money = withDrewMoney;
    }
}
