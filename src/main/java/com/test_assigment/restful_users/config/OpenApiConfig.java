package com.test_assigment.restful_users.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.DEFAULT)
@OpenAPIDefinition(
        info = @Info(title = "RESTful API 'Users'",
                description = """
                        Test task for job apply. RESTful API with endpoint "rest-api/users" and followed requirements:
                                                
                        Requirements:
                        1. It has the following fields:
                                                
                        1.1. Email (required). Add validation against email pattern
                                                
                        1.2. First name (required)
                                                
                        1.3. Last name (required)
                                                
                        1.4. Birth date (required). Value must be earlier than current date
                                                
                        1.5. Address (optional)
                                                
                        1.6. Phone number (optional)
                                                
                        2. It has the following functionality:
                                                
                        2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
                                                
                        2.2. Update one/some user fields
                                                
                        2.3. Update all user fields
                                                
                        2.4. Delete user
                                                
                        2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
                                                
                        3. Code is covered by unit tests using Spring
                                                
                        4. Code has error handling for REST
                                                
                        5. API responses are in JSON format
                                                          
                        """,
                version = "v1.0",
                contact = @Contact(url = "https://www.linkedin.com/in/denys-filonenko-6a8632163/",
                        name = "Denys Filonenko", email = "filonenko.denys94@gmail.com"))
)
public class OpenApiConfig {
}
