package com.skoneczny.api;

import java.util.List;

import com.skoneczny.entites.Address;
import com.skoneczny.entites.User;

public interface IAddressService {
		
	void saveAdress(Address adres);
	void deleteAdress(Address adres);
	List<Address> getAddress(User user);

}
