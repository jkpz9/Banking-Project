package com.userFront.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.userFront.domain.Ricipient;

public interface RicipientDao extends CrudRepository<Ricipient, Long> {
	
	List<Ricipient> findAll();

    Ricipient findByName(String recipientName);

    void deleteByName(String recipientName);

}
