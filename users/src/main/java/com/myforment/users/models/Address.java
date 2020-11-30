package com.myforment.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	private String addressLineOne;
	private String addressLineTwo;
	private String cap;
	private String city;
	private String province;
	private String country;

}
