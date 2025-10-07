package com.spring.hrm.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.dto.AccountViewDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    
    @Mapping(source = "staffId", target = "staffId")
    Account toAccount(RegisterRequestDto dto);
    
    @Mapping(source = "username", target = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "lastLoginDateDisplay", target = "lastLoginDateDisplay")
    @Mapping(source = "createdAt", target = "createdAt")
    AccountViewDto fromAccount(Account account);

    @Mapping(target = "staffId", ignore = true)
    void updateAccountFromDto(RegisterRequestDto dto, @MappingTarget Account account);
    
}
