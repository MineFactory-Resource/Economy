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
    public void set(String economyID, long value) {
        this.moneyMap.put(economyID, value);
    }
}
