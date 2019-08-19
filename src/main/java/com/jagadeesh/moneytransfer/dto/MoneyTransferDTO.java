package com.jagadeesh.moneytransfer.dto;

import java.math.BigDecimal;

final public class MoneyTransferDTO {

    private final String source;
    private final String target;
    private final BigDecimal amount;

    public MoneyTransferDTO(String source, String target, String amount) {
        this.source = source;
        this.target = target;
        this.amount = new BigDecimal(amount);
    }

    public MoneyTransferDTO() {
        //default constructor added for jaxb compatibility
        source = "";
        target = "";
        amount = BigDecimal.ZERO;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "MoneyTransferDTO | amount=" + amount + " " +
                "from sourceAccount='" + source + '\'' +
                "to targetAccount='" + target;
    }
}
