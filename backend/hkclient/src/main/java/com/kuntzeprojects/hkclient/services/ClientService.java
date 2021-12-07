package com.kuntzeprojects.hkclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuntzeprojects.hkclient.dto.ClientDTO;
import com.kuntzeprojects.hkclient.entities.Client;
import com.kuntzeprojects.hkclient.repositories.ClientRepository;
import com.kuntzeprojects.hkclient.services.exceptions.DatabaseException;
import com.kuntzeprojects.hkclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Este recurso não foi encontrado."));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO obj) {
		Client entity = new Client();
		dtoToEntity(obj, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO obj) {
		try {
			Client entity = repository.getOne(id);
			dtoToEntity(obj, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID "+id+" não foi encontrado.");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private void dtoToEntity(ClientDTO obj, Client entity) {
		entity.setName(obj.getName());
		entity.setCpf(obj.getCpf());
		entity.setBirthDate(obj.getBirthDate());
		entity.setChildren(obj.getChildren());
		entity.setIncome(obj.getIncome());
	}
}
