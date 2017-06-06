## Elos - Guia do Usuário
![Build status](https://travis-ci.org/jonathanarodr/elos.svg?branch=master)

- [Introdução](#Introducao)
- [Usando Elos com Maven](#Instalacao)
- [Configuração](#Configuracao)
- [Aprendendo o Elos](#Documentacao)

## <a name="Introducao"></a>Introdução
Elos é um microframework web desenvolvido na linguagem Java no qual possui como principal objetivo simplificar a escrita de códigos complexos em rotinas simples de fácil compreensão tornando o desenvolvimento Java algo mais produtivo e agradável.

## <a name="Instalacao"></a>Usando Elos com Maven
Elos utiliza o Maven para gerenciar suas dependências, portanto, certifique-se de ter instalado o Maven em sua máquina, feito isto adicione a seguinte dependência em seu arquivo `pom.xml`:

```xml
<dependencies>
    <!-- https://github.com/jonathanarodr/elos-->
    <dependency>
        <groupId>br.com.elos</groupId>
        <artifactId>elos</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

## <a name="Configuracao"></a>Configuração
Depois de baixado, você deve configurar seu projeto para possibilitar o uso das funcionalidades presentes no Elos. Esta configuração é bem simples, para isto, extenda a classe `App.java` e configure as propriedades de cada pacote conforme desejar.

**Exemplo de configuração:**

```java
package br.com.company.example;

import br.com.elos.App;

public class AppConfig extends App {

    public AppConfig() {
        super();
        server_url = "http://localhost:8080/";
        resource = "br.com.company.example.controller";
        db_hostname = "127.0.0.1";
        db_port = 3306;
        db_database = "dbname";
        db_username = "root";
        db_password = "123";
        storage_location = "http://localhost:8080/public/storage/";
    }

}

```

## <a name="Documentacao"></a>Aprendendo o Elos
Para conhecer mais sobre as funcionalidades do Elos, criamos uma [documentação completa](https://github.com/jonathanarodr/elos/wiki) objetiva para você em nosso **Wiki do GitHub**.
