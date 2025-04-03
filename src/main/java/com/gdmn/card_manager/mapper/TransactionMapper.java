package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.TransactionData;
import com.gdmn.card_manager.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<TransactionData> map(List<Transaction> t) {
        return t.stream()
                .map(this::map)
                .toList();
    }
}
