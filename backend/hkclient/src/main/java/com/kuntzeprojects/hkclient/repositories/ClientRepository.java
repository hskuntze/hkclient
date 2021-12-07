package com.kuntzeprojects.hkclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kuntzeprojects.hkclient.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
