package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfig {
	
	@Bean
	OpenAPI springBlogPessoalOpenAPI() { //Classe que gera a documentação no Swagger
		return new OpenAPI()
				.info(new Info()
						.title("Projeto Blog Pessoal") // infos sobre a API
						.description("Projeto Blog Pessoal - Generation Brasil")
						.version("v0.0.1")
						.license(new License() // info sobre as licenças
							.name("Paola Patricia")
							.url("https://github.com/PaolaPatricia16/blogpessoal"))
						.contact(new Contact()
								.name("Paola Patricia")
								.url("https://www.linkedin.com/in/paola-patricia-9bba6b15a")
								.email("patricia-9856@hotmail.com")))
				.externalDocs(new ExternalDocumentation()
						.description("Github")
						.url("https://github.com/PaolaPatricia16/blogpessoal"));
	}
	
	@Bean
	OpenApiCustomizer customizerGlobalHeaderOpenApiCustomizer() { //personaliza a Swagger, todas as Https
		
		return openApi ->{ // cria obj da classe OpenAPI
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations() // loop para leitura das paths
					.forEach(operation -> {  // faz a leitura dos https e subsitui a msg
						
						ApiResponses apiResponses = operation.getResponses(); // recebe as respostas http de cada endpoint
						
						apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
						apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
						apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
						apiResponses.addApiResponse("400", createApiResponse("Erro na requisição!"));
						apiResponses.addApiResponse("401", createApiResponse("Acesso não autorizado!"));
						apiResponses.addApiResponse("403", createApiResponse("Acesso proibido!"));
						apiResponses.addApiResponse("404", createApiResponse("Objeto não encontrado!"));
						apiResponses.addApiResponse("500", createApiResponse("Erro na aplicação!"));
						
					}));
		};
	}
	
	private ApiResponse createApiResponse (String message) { // adicionar uma mensagem a cada resposta de http
		return new ApiResponse().description(message);
	}

}
