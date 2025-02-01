package com.olx.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
		info = @Info(
				title = "OLX REST API Documentation",
				description = "OLX Management application",
				version = "1.1",
				license = @License(
						name = "LGPL",
						url = "http://lgpl.com"
				),
				contact = @Contact(
						name = "Habibulla",
						email = "habibulla@gmail.com"
				)
		)		
)
public class ApiConfig {

}
