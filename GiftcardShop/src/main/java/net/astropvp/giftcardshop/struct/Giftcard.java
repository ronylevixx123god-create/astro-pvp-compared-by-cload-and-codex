package net.astropvp.giftcardshop.struct;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Giftcard {
    private final String creationDate;
    private final UUID uuid;
    private final double amount;
    private final String code;


    public Giftcard(UUID uuid, String codeAndAmount, String date, double amount) {
        this.creationDate = date;
        this.uuid = uuid;
        this.code = codeAndAmount.split(":")[0];
        this.amount = amount;
    }

}
