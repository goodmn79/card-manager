package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.TransactionData;
import com.gdmn.card_manager.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionData map(Transaction t) {
        return new TransactionData()
                .setId(t.getId())
                .setCardId(t.getCard().getId())
                .setType(t.getType())
                .setTimestamp(t.getTimestamp())
                .setAmount(t.getAmount())
                .setDescription(t.getDescription());
    }

    public Page<TransactionData> map(Page<Transaction> t) {
        return t.map(this::map);
    }
}
