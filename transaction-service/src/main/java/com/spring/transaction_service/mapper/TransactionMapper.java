package com.spring.transaction_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import com.spring.transaction_service.dtos.TransactionRequestDTO;
import com.spring.transaction_service.dtos.TransactionResponseDTO;
import com.spring.transaction_service.dtos.PaymentGatewayLogsDTO;
import com.spring.transaction_service.entity.Transaction;
import com.spring.transaction_service.entity.PaymentGatewayLogs;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toEntity(TransactionRequestDTO requestDTO);

    TransactionResponseDTO toResponseDTO(Transaction entity);

    PaymentGatewayLogsDTO toPaymentGatewayLogsDTO(PaymentGatewayLogs entity);

    PaymentGatewayLogs toPaymentGatewayLogsEntity(PaymentGatewayLogsDTO dto);
}
