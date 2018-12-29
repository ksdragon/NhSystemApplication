package com.skoneczny.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skoneczny.api.IAddressService;
import com.skoneczny.entites.Address;
import com.skoneczny.entites.User;
import com.skoneczny.repositories.AddressRepository;

@Service
public class AddressService implements IAddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public void saveAdress(Address address) {
		addressRepository.save(address);		
	}

	@Override
	public void deleteAdress(Address address) {	
		addressRepository.delete(address);
	}

	@Override
	public List<Address> getAddress(User user) {
			List<Address> address = new ArrayList<Address>();
			address = addressRepository.findByUser(user);			
		return address;
	}

}
